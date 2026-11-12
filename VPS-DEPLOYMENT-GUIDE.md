# VPS Deployment Guide - OVH VPS-1 2027

Complete guide for deploying MSSD project to OVH VPS-1 2027 specifications.

---

## VPS Specifications (OVH VPS-1 2027)

```
Location:        Europe (France - Strasbourg)
Processor:       2 vCore
Memory:          4GB RAM
Storage:         40GB SSD NVMe
OS:              Ubuntu 26.04
Backup:          Option Automated Backup Standard
Storage Type:    Option Local Storage
Billing:         1 month
```

---

## Pre-Deployment Checklist

### 1. VPS Setup Prerequisites
- [ ] VPS activated and running
- [ ] Root SSH access configured
- [ ] SSH key uploaded (for security)
- [ ] Ubuntu 26.04 fresh installation
- [ ] Static IP assigned
- [ ] Domain name registered
- [ ] DNS records prepared

### 2. Network Configuration
- [ ] Port 22 (SSH) open
- [ ] Port 80 (HTTP) open
- [ ] Port 443 (HTTPS) open
- [ ] Port 3306 (MySQL) closed (internal only)
- [ ] Firewall configured
- [ ] DDoS protection enabled (if available)

### 3. Local Preparation
- [ ] Project files ready
- [ ] Docker Compose files prepared
- [ ] SSL certificates generated or Let's Encrypt ready
- [ ] Environment configuration (.env) prepared
- [ ] Backup strategy defined
- [ ] Domain pointing to VPS IP

---

## Step 1: Initial VPS Setup

### 1.1 Connect via SSH
```bash
ssh root@YOUR_VPS_IP
# or with SSH key
ssh -i ~/.ssh/your_key root@YOUR_VPS_IP
```

### 1.2 Update System
```bash
apt update
apt upgrade -y
apt autoremove -y
```

### 1.3 Create Non-Root User (Recommended)
```bash
# Create user
adduser deployer

# Add to sudo group
usermod -aG sudo deployer

# Add SSH key for new user (as root)
mkdir -p /home/deployer/.ssh
cp ~/.ssh/authorized_keys /home/deployer/.ssh/
chown -R deployer:deployer /home/deployer/.ssh
chmod 700 /home/deployer/.ssh
chmod 600 /home/deployer/.ssh/authorized_keys

# Switch to new user
su - deployer
```

### 1.4 Configure Firewall (UFW)
```bash
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable
```

### 1.5 Configure SSH Hardening
```bash
sudo nano /etc/ssh/sshd_config
```

**Key changes:**
```
PermitRootLogin no
PasswordAuthentication no
X11Forwarding no
MaxAuthTries 3
MaxSessions 5
```

**Restart SSH:**
```bash
sudo systemctl restart sshd
```

---

## Step 2: Install Docker & Docker Compose

### 2.1 Install Docker
```bash
# Add Docker GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

# Add Docker repository
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# Update and install
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# Add current user to docker group
sudo usermod -aG docker $USER

# Logout and login to apply group changes
exit
```

Reconnect SSH and verify:
```bash
docker --version
```

### 2.2 Install Docker Compose
```bash
# Download Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Make executable
sudo chmod +x /usr/local/bin/docker-compose

# Verify
docker-compose --version
```

### 2.3 Start Docker Service
```bash
sudo systemctl enable docker
sudo systemctl start docker
sudo systemctl status docker
```

---

## Step 3: Prepare Project Files

### 3.1 Create Project Directory
```bash
# Create directory structure
sudo mkdir -p /opt/mssd
sudo chown -R $USER:$USER /opt/mssd

# Create subdirectories
mkdir -p /opt/mssd/certs
mkdir -p /opt/mssd/backups
mkdir -p /opt/mssd/logs
```

### 3.2 Clone/Upload Project
```bash
# Option A: Git clone
cd /opt/mssd
git clone https://github.com/YOUR_REPO/MSSD-mssd.git .

# Option B: Upload via SCP
scp -r ./MSSD-mssd/* deployer@YOUR_VPS_IP:/opt/mssd/

# Option C: Download specific files
cd /opt/mssd
wget https://your-repo/docker-compose.ssl.yml
wget https://your-repo/nginx-ssl.conf
wget https://your-repo/.env.production
# ... etc
```

### 3.3 Verify Files
```bash
ls -la /opt/mssd/
# Should see: docker-compose.ssl.yml, nginx-ssl.conf, etc.
```

---

## Step 4: SSL Certificate Setup

### 4.1 Generate Self-Signed Certificate (Development)
```bash
cd /opt/mssd

# Generate certificate valid for 365 days
openssl req -x509 -newkey rsa:4096 -keyout certs/key.pem -out certs/cert.pem -days 365 -nodes \
  -subj "/C=FR/ST=Strasbourg/L=Strasbourg/O=MSSD/CN=yourdomain.com"

# Set permissions
chmod 644 certs/cert.pem
chmod 600 certs/key.pem
```

### 4.2 Use Let's Encrypt (Production - Recommended)
```bash
# Install Certbot
sudo apt install -y certbot python3-certbot-nginx

# Generate certificate
sudo certbot certonly --standalone -d yourdomain.com -d www.yourdomain.com

# Copy to project directory
sudo cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem /opt/mssd/certs/cert.pem
sudo cp /etc/letsencrypt/live/yourdomain.com/privkey.pem /opt/mssd/certs/key.pem
sudo chown $USER:$USER /opt/mssd/certs/*
```

### 4.3 Auto-Renewal Script (Let's Encrypt)
```bash
# Create renewal script
cat > /opt/mssd/renew-certs.sh << 'EOF'
#!/bin/bash
certbot renew --quiet
cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem /opt/mssd/certs/cert.pem
cp /etc/letsencrypt/live/yourdomain.com/privkey.pem /opt/mssd/certs/key.pem
cd /opt/mssd
docker-compose -f docker-compose.ssl.yml restart mssd-nginx
EOF

chmod +x /opt/mssd/renew-certs.sh

# Add to crontab (renewal at 3 AM daily)
(crontab -l 2>/dev/null; echo "0 3 * * * /opt/mssd/renew-certs.sh") | crontab -
```

---

## Step 5: Configure Environment

### 5.1 Create .env File
```bash
# Copy production template
cp /opt/mssd/.env.production /opt/mssd/.env

# Edit with your settings
nano /opt/mssd/.env
```

### 5.2 Configure .env for VPS
Update these critical values:

```env
# Database
MYSQL_ROOT_PASSWORD=<GENERATE_STRONG_PASSWORD>
MYSQL_USER=mssd_admin
MYSQL_PASSWORD=<GENERATE_STRONG_PASSWORD>
MYSQL_DATABASE=mssd_production

# Domain
APP_DOMAIN=yourdomain.com
APP_URL=https://yourdomain.com
FRONTEND_API_BASE_URL=https://yourdomain.com/api

# Security
JWT_SECRET=<GENERATE_WITH: openssl rand -base64 32>
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com

# Email (Optional)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=<APP_PASSWORD>

# Backend
SPRING_PROFILES_ACTIVE=prod
LOG_LEVEL=INFO

# Performance
DB_POOL_SIZE=15
CACHE_ENABLED=true
CACHE_TTL=3600

# Backup
BACKUP_ENABLED=true
BACKUP_SCHEDULE=0 2 * * *
```

### 5.3 Secure .env File
```bash
chmod 600 /opt/mssd/.env
# Do NOT commit to version control
echo ".env" >> /opt/mssd/.gitignore
```

---

## Step 6: Resource Optimization for VPS

### 6.1 Update docker-compose.ssl.yml for Limited Resources

Edit `/opt/mssd/docker-compose.ssl.yml` and add resource limits:

```yaml
services:
  mssd-backend:
    # ... existing config ...
    deploy:
      resources:
        limits:
          cpus: '1.5'
          memory: 1.5G
        reservations:
          cpus: '1'
          memory: 1G

  mssd-frontend:
    # ... existing config ...
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M

  mssd-db:
    # ... existing config ...
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 1.5G
    environment:
      MYSQL_MAX_CONNECTIONS: 50
```

### 6.2 Kernel Parameters (for better performance)
```bash
sudo sysctl -w vm.swappiness=10
sudo sysctl -w net.core.somaxconn=1024
sudo sysctl -w net.ipv4.tcp_max_syn_backlog=2048

# Make persistent
sudo tee -a /etc/sysctl.conf << EOF
vm.swappiness=10
net.core.somaxconn=1024
net.ipv4.tcp_max_syn_backlog=2048
EOF

sudo sysctl -p
```

---

## Step 7: Deploy Application

### 7.1 Pre-Deployment Verification
```bash
cd /opt/mssd

# Verify Docker files
docker-compose -f docker-compose.ssl.yml config

# Check disk space
df -h

# Check available memory
free -h
```

### 7.2 Build Images (if needed)
```bash
cd /opt/mssd

# Build backend
docker-compose -f docker-compose.ssl.yml build mssd-backend

# Build frontend
docker-compose -f docker-compose.ssl.yml build mssd-frontend

# Check images
docker images | grep mssd
```

### 7.3 Start Services
```bash
# Start all services
cd /opt/mssd
docker-compose -f docker-compose.ssl.yml up -d

# Wait 30 seconds for services to initialize
sleep 30

# Check status
docker-compose ps
```

### 7.4 Verify Deployment
```bash
# Check service status
docker-compose ps

# Check logs
docker-compose logs --tail=50

# Test frontend
curl -k https://yourdomain.com

# Test backend
curl -k https://yourdomain.com/api/health

# Check database
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SHOW DATABASES;"
```

---

## Step 8: Backup Configuration

### 8.1 Create Backup Directory
```bash
mkdir -p /opt/mssd/backups
chmod 755 /opt/mssd/backups
```

### 8.2 Backup Script
```bash
cat > /opt/mssd/backup-daily.sh << 'EOF'
#!/bin/bash

BACKUP_DIR="/opt/mssd/backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/mysql_backup_$TIMESTAMP.sql"

# Create backup
docker-compose -f docker-compose.ssl.yml exec -T mssd-db \
  mysqldump -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE > "$BACKUP_FILE"

# Compress
gzip "$BACKUP_FILE"

# Keep only last 30 days
find "$BACKUP_DIR" -name "mysql_backup_*.sql.gz" -mtime +30 -delete

echo "Backup completed: ${BACKUP_FILE}.gz"
EOF

chmod +x /opt/mssd/backup-daily.sh
```

### 8.3 Schedule Daily Backups
```bash
# Add to crontab (2 AM daily)
(crontab -l 2>/dev/null; echo "0 2 * * * cd /opt/mssd && ./backup-daily.sh") | crontab -

# Verify
crontab -l
```

---

## Step 9: Monitoring & Maintenance

### 9.1 Monitor Services
```bash
# Real-time monitoring
docker stats

# Service status
docker-compose ps

# View logs
docker-compose logs -f

# Specific service logs
docker-compose logs -f mssd-backend
```

### 9.2 Check System Resources
```bash
# Disk usage
df -h

# Memory usage
free -h

# CPU usage
top

# VPS load
uptime
```

### 9.3 Database Maintenance
```bash
# Check database size
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e \
  "SELECT table_schema, ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb FROM information_schema.TABLES GROUP BY table_schema;"

# Optimize tables
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e \
  "OPTIMIZE TABLE db_name.table_name;"
```

---

## Step 10: Enable Auto-Start on Reboot

### 10.1 Create Systemd Service
```bash
sudo tee /etc/systemd/system/mssd-docker.service << EOF
[Unit]
Description=MSSD Docker Compose Application
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
WorkingDirectory=/opt/mssd
ExecStart=/usr/local/bin/docker-compose -f docker-compose.ssl.yml up -d
ExecStop=/usr/local/bin/docker-compose -f docker-compose.ssl.yml down
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
EOF

# Enable service
sudo systemctl daemon-reload
sudo systemctl enable mssd-docker.service

# Start service
sudo systemctl start mssd-docker.service

# Check status
sudo systemctl status mssd-docker.service
```

---

## Step 11: Reverse Proxy & SSL (Optional - If Using Separate Nginx)

### 11.1 Install Nginx
```bash
sudo apt install -y nginx

# Enable
sudo systemctl enable nginx
sudo systemctl start nginx
```

### 11.2 Configure Nginx
```bash
sudo tee /etc/nginx/sites-available/mssd << 'EOF'
upstream backend {
    server localhost:8080;
}

server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;

    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    location / {
        proxy_pass http://localhost:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /api/ {
        proxy_pass http://backend/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF

# Enable
sudo ln -s /etc/nginx/sites-available/mssd /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

---

## Step 12: Performance Tuning

### 12.1 Optimize Docker
```bash
# Set log rotation
cat > /etc/docker/daemon.json << 'EOF'
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  },
  "storage-driver": "overlay2"
}
EOF

sudo systemctl restart docker
```

### 12.2 Swap Configuration (For 4GB VPS)
```bash
# Check current swap
free -h

# If no swap, create 4GB swap file
sudo fallocate -l 4G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# Make permanent
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Verify
free -h
```

---

## Step 13: Security Hardening

### 13.1 Fail2Ban Installation
```bash
# Install
sudo apt install -y fail2ban

# Configure
sudo tee /etc/fail2ban/jail.local << 'EOF'
[DEFAULT]
bantime = 3600
findtime = 600
maxretry = 5

[sshd]
enabled = true
EOF

# Start
sudo systemctl enable fail2ban
sudo systemctl start fail2ban
```

### 13.2 Additional Security
```bash
# Disable root login password
sudo passwd -l root

# Automatic security updates
sudo apt install -y unattended-upgrades
sudo dpkg-reconfigure -plow unattended-upgrades
```

---

## Step 14: Verification & Testing

### 14.1 Full System Check
```bash
# 1. Check DNS resolution
nslookup yourdomain.com

# 2. Check SSL certificate
curl -v https://yourdomain.com

# 3. Check frontend
curl -s https://yourdomain.com | head -20

# 4. Check backend health
curl -k https://yourdomain.com/api/health

# 5. Check database connectivity
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SELECT 1"

# 6. Check disk space
df -h

# 7. Check memory
free -h

# 8. Check CPU
uptime
```

### 14.2 Performance Test
```bash
# Install Apache Bench
sudo apt install -y apache2-utils

# Test frontend performance
ab -n 100 -c 10 https://yourdomain.com

# Test API performance
ab -n 100 -c 10 https://yourdomain.com/api/health
```

---

## Troubleshooting

### Services Won't Start
```bash
# Check logs
docker-compose logs

# Check Docker daemon
sudo systemctl status docker

# Restart Docker
sudo systemctl restart docker
```

### SSL Certificate Issues
```bash
# Check certificate expiration
openssl x509 -in certs/cert.pem -noout -dates

# Renew manually
sudo certbot renew --force-renewal
```

### Out of Memory
```bash
# Check memory usage
free -h
docker stats

# Reduce container limits or add swap
```

### Database Connection Error
```bash
# Check if database is running
docker-compose ps mssd-db

# Test connection
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SELECT 1"
```

---

## Maintenance Schedule

### Daily
- [ ] Check service status: `docker-compose ps`
- [ ] Monitor logs: `docker-compose logs`
- [ ] Verify backups created

### Weekly
- [ ] Review error logs
- [ ] Check disk space: `df -h`
- [ ] Check memory: `free -h`

### Monthly
- [ ] Test database restore
- [ ] Review security logs
- [ ] Check SSL certificate expiration
- [ ] Performance analysis

### Quarterly
- [ ] Full system security audit
- [ ] Backup retention review
- [ ] Update Docker images
- [ ] Documentation review

---

## Useful Commands

```bash
# SSH into VPS
ssh deployer@YOUR_VPS_IP

# Navigate to project
cd /opt/mssd

# View services
docker-compose ps

# View logs
docker-compose logs -f

# Restart services
docker-compose restart

# Stop services
docker-compose down

# Backup database
docker-compose exec mssd-db mysqldump -u root -p$MYSQL_ROOT_PASSWORD $MYSQL_DATABASE > backup.sql

# Check disk space
df -h

# Check memory
free -h

# Monitor resources
docker stats

# View cron jobs
crontab -l
```

---

## Cost & Capacity Notes

**VPS-1 2027 Capacity:**
- 2 vCore: Suitable for this project
- 4GB RAM: Adequate (backend ~1.5GB, frontend ~512MB, DB ~1.5GB)
- 40GB SSD: Sufficient (OS ~20GB, application ~5GB, data ~10GB)

**Recommendations:**
- Enable automated backups (OVH option)
- Monitor disk usage monthly
- Plan for database growth
- Consider upgrade if traffic increases significantly

---

## Support & Resources

- OVH VPS Documentation: https://docs.ovh.com/gb/en/vps/
- Docker Documentation: https://docs.docker.com/
- Ubuntu Documentation: https://ubuntu.com/support
- Let's Encrypt: https://letsencrypt.org/

---

**Version:** 1.0  
**Last Updated:** 2024  
**Status:** Ready for VPS Deployment
