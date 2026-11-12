#!/bin/bash

# ============================================
# MSSD Deployment to VPS Script
# One-command deployment to OVH VPS
# ============================================

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
VPS_USER="${VPS_USER:-deployer}"
VPS_HOST="${1:-}"
PROJECT_DIR="/opt/mssd"
DOMAIN="${DOMAIN:-}"

# Functions
print_header() {
  echo -e "${BLUE}════════════════════════════════════════${NC}"
  echo -e "${BLUE}$1${NC}"
  echo -e "${BLUE}════════════════════════════════════════${NC}\n"
}

print_success() {
  echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
  echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
  echo -e "${RED}✗ $1${NC}"
}

# Validation
if [ -z "$VPS_HOST" ]; then
  print_error "Usage: $0 <VPS_IP_OR_DOMAIN>"
  echo "Example: $0 123.45.67.89"
  exit 1
fi

if [ -z "$DOMAIN" ]; then
  print_error "Please set DOMAIN environment variable"
  echo "Example: export DOMAIN=yourdomain.com"
  exit 1
fi

print_header "MSSD VPS Deployment"

echo "Configuration:"
echo "  VPS Host: $VPS_HOST"
echo "  VPS User: $VPS_USER"
echo "  Domain:   $DOMAIN"
echo "  Project:  $PROJECT_DIR"
echo ""

# Confirm
read -p "Continue with deployment? (y/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
  print_warning "Deployment cancelled"
  exit 1
fi

# Step 1: Connect and verify SSH
print_header "Step 1: Verifying SSH Connection"
if ssh -o ConnectTimeout=5 "$VPS_USER@$VPS_HOST" "echo 'SSH connection successful'" &>/dev/null; then
  print_success "SSH connection successful"
else
  print_error "Cannot connect via SSH"
  echo "Please check:"
  echo "  - VPS IP/domain is correct"
  echo "  - SSH key is configured"
  echo "  - Firewall allows SSH"
  exit 1
fi

# Step 2: Verify Docker installation
print_header "Step 2: Verifying Docker Installation"
if ssh "$VPS_USER@$VPS_HOST" "docker --version" &>/dev/null; then
  print_success "Docker is installed"
  docker_version=$(ssh "$VPS_USER@$VPS_HOST" "docker --version")
  echo "  $docker_version"
else
  print_warning "Docker not found - installing..."
  ssh "$VPS_USER@$VPS_HOST" << 'DOCKER_INSTALL'
sudo apt-get update
sudo apt-get install -y docker.io docker-compose
sudo usermod -aG docker $USER
sudo systemctl enable docker
sudo systemctl start docker
DOCKER_INSTALL
  print_success "Docker installed"
fi

# Step 3: Create project directory
print_header "Step 3: Preparing Project Directory"
ssh "$VPS_USER@$VPS_HOST" << 'DIR_SETUP'
sudo mkdir -p /opt/mssd
sudo chown -R $(whoami):$(whoami) /opt/mssd
mkdir -p /opt/mssd/certs
mkdir -p /opt/mssd/backups
mkdir -p /opt/mssd/scripts
DIR_SETUP
print_success "Project directory created"

# Step 4: Upload files
print_header "Step 4: Uploading Project Files"
echo "Uploading Docker Compose files..."
scp docker-compose.ssl.yml "$VPS_USER@$VPS_HOST:$PROJECT_DIR/"
scp nginx-ssl.conf "$VPS_USER@$VPS_HOST:$PROJECT_DIR/"
scp prometheus.yml "$VPS_USER@$VPS_HOST:$PROJECT_DIR/"
echo "Uploading backend Dockerfile..."
scp -r mssd-backend/Dockerfile* "$VPS_USER@$VPS_HOST:$PROJECT_DIR/mssd-backend/"
echo "Uploading frontend Dockerfile..."
scp -r mssd-frontend/Dockerfile* "$VPS_USER@$VPS_HOST:$PROJECT_DIR/mssd-frontend/"
echo "Uploading scripts..."
scp scripts/*.sh "$VPS_USER@$VPS_HOST:$PROJECT_DIR/scripts/"
print_success "Files uploaded"

# Step 5: SSL Certificate
print_header "Step 5: Setting Up SSL Certificate"

# Check if Let's Encrypt is available
if ssh "$VPS_USER@$VPS_HOST" "which certbot" &>/dev/null; then
  print_success "Let's Encrypt (Certbot) available"
  
  # Prompt for SSL generation
  read -p "Generate Let's Encrypt certificate? (y/n) " -n 1 -r
  echo
  
  if [[ $REPLY =~ ^[Yy]$ ]]; then
    ssh "$VPS_USER@$VPS_HOST" << CERTBOT_SETUP
sudo apt-get install -y certbot python3-certbot-nginx
sudo certbot certonly --standalone -d $DOMAIN -d www.$DOMAIN --agree-tos --register-unsafely-without-email -n
sudo cp /etc/letsencrypt/live/$DOMAIN/fullchain.pem $PROJECT_DIR/certs/cert.pem
sudo cp /etc/letsencrypt/live/$DOMAIN/privkey.pem $PROJECT_DIR/certs/key.pem
sudo chown $VPS_USER:$VPS_USER $PROJECT_DIR/certs/*
CERTBOT_SETUP
    print_success "SSL certificate generated"
  fi
else
  print_warning "Let's Encrypt not available - using self-signed certificate"
  ssh "$VPS_USER@$VPS_HOST" << 'SELFCERT'
cd /opt/mssd
openssl req -x509 -newkey rsa:4096 -keyout certs/key.pem -out certs/cert.pem -days 365 -nodes \
  -subj "/C=FR/ST=Strasbourg/L=Strasbourg/O=MSSD/CN=$DOMAIN"
chmod 644 certs/cert.pem
chmod 600 certs/key.pem
SELFCERT
  print_warning "Self-signed certificate created - renew before production"
fi

# Step 6: Configure environment
print_header "Step 6: Configuring Environment"

# Generate secure password
DB_ROOT_PASS=$(openssl rand -base64 16)
DB_USER_PASS=$(openssl rand -base64 16)
JWT_SECRET=$(openssl rand -base64 32)

# Create .env file on VPS
ssh "$VPS_USER@$VPS_HOST" << ENVFILE
cat > $PROJECT_DIR/.env << 'EOF'
MYSQL_ROOT_PASSWORD=$DB_ROOT_PASS
MYSQL_USER=mssd_admin
MYSQL_PASSWORD=$DB_USER_PASS
MYSQL_DATABASE=mssd_production

APP_DOMAIN=$DOMAIN
APP_URL=https://$DOMAIN
FRONTEND_API_BASE_URL=https://$DOMAIN/api

SPRING_PROFILES_ACTIVE=prod
LOG_LEVEL=INFO
JWT_SECRET=$JWT_SECRET

DB_POOL_SIZE=15
CACHE_ENABLED=true
CACHE_TTL=3600

PROMETHEUS_ENABLED=true
METRICS_ENABLED=true
BACKUP_ENABLED=true
EOF

chmod 600 $PROJECT_DIR/.env
ENVFILE

print_success "Environment configured"
echo "Generated credentials:"
echo "  DB Root Password:    $DB_ROOT_PASS"
echo "  DB User Password:    $DB_USER_PASS"
echo "  JWT Secret:          $JWT_SECRET"
echo ""
print_warning "Save these credentials in a secure location!"

# Step 7: Build and deploy
print_header "Step 7: Building and Starting Services"

ssh "$VPS_USER@$VPS_HOST" << 'DEPLOY'
cd /opt/mssd
docker-compose -f docker-compose.ssl.yml build --no-cache
docker-compose -f docker-compose.ssl.yml up -d
DEPLOY

print_success "Services started"

# Wait for services to initialize
echo "Waiting for services to initialize..."
sleep 30

# Step 8: Verify deployment
print_header "Step 8: Verifying Deployment"

# Check services
echo "Service status:"
ssh "$VPS_USER@$VPS_HOST" "cd $PROJECT_DIR && docker-compose -f docker-compose.ssl.yml ps"

# Check frontend
print_header "Testing Application"
if curl -k -s https://$DOMAIN > /dev/null; then
  print_success "Frontend accessible"
else
  print_warning "Could not reach frontend - firewall might not be configured"
fi

# Step 9: Setup backups
print_header "Step 9: Configuring Backups"

ssh "$VPS_USER@$VPS_HOST" << 'BACKUP_SETUP'
# Create backup script
cat > /opt/mssd/backup-daily.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/opt/mssd/backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/mysql_backup_$TIMESTAMP.sql"

docker-compose -f docker-compose.ssl.yml exec -T mssd-db \
  mysqldump -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE > "$BACKUP_FILE"

gzip "$BACKUP_FILE"
find "$BACKUP_DIR" -name "mysql_backup_*.sql.gz" -mtime +30 -delete
EOF

chmod +x /opt/mssd/backup-daily.sh

# Add cron job (2 AM daily)
(crontab -l 2>/dev/null; echo "0 2 * * * cd /opt/mssd && ./backup-daily.sh") | crontab -
BACKUP_SETUP

print_success "Backups scheduled (daily at 2 AM)"

# Step 10: Auto-start on reboot
print_header "Step 10: Configuring Auto-Start"

ssh "$VPS_USER@$VPS_HOST" << 'AUTOSTART'
sudo tee /etc/systemd/system/mssd-docker.service << 'EOF'
[Unit]
Description=MSSD Docker Compose Application
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
WorkingDirectory=/opt/mssd
ExecStart=/usr/bin/docker-compose -f docker-compose.ssl.yml up -d
ExecStop=/usr/bin/docker-compose -f docker-compose.ssl.yml down
RemainAfterExit=yes
User=deployer

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable mssd-docker.service
AUTOSTART

print_success "Auto-start configured"

# Step 11: Firewall setup
print_header "Step 11: Configuring Firewall"

ssh "$VPS_USER@$VPS_HOST" << 'FIREWALL'
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable
FIREWALL

print_success "Firewall configured"

# Final summary
print_header "Deployment Complete!"

echo "Access Information:"
echo "  Frontend:   https://$DOMAIN"
echo "  Backend:    https://$DOMAIN/api"
echo "  SSH:        ssh $VPS_USER@$VPS_HOST"
echo ""
echo "Useful Commands:"
echo "  View logs:     ssh $VPS_USER@$VPS_HOST 'cd $PROJECT_DIR && docker-compose logs -f'"
echo "  Check status:  ssh $VPS_USER@$VPS_HOST 'cd $PROJECT_DIR && docker-compose ps'"
echo "  Backup DB:     ssh $VPS_USER@$VPS_HOST 'bash $PROJECT_DIR/backup-daily.sh'"
echo ""
echo "Next Steps:"
echo "  1. Update DNS records to point to VPS IP"
echo "  2. Test application at https://$DOMAIN"
echo "  3. Review DOCKER-SETUP-GUIDE.md for maintenance"
echo "  4. Setup monitoring and alerts"
echo ""

print_success "Deployment successful!"
