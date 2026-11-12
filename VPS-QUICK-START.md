# VPS Quick Start - OVH VPS-1 2027

Fast deployment of MSSD to your OVH VPS.

---

## Prerequisites (10 minutes)

1. **VPS Activated**
   - [ ] OVH VPS-1 2027 running
   - [ ] Ubuntu 26.04 installed
   - [ ] Root access via SSH
   - [ ] Static IP assigned

2. **Local Setup**
   - [ ] Domain registered and configured
   - [ ] SSH key generated: `ssh-keygen -t rsa -b 4096`
   - [ ] SSH key uploaded to VPS
   - [ ] Project files cloned

---

## One-Command Deployment (30 minutes)

### 1. Set Environment Variables

```bash
export DOMAIN=yourdomain.com        # Your domain
export VPS_USER=deployer            # SSH user
export VPS_HOST=YOUR_VPS_IP         # VPS IP address
```

### 2. Run Deployment Script

```bash
# Make script executable
chmod +x scripts/deploy-to-vps.sh

# Execute deployment
bash scripts/deploy-to-vps.sh $VPS_HOST
```

**Script will automatically:**
- ✓ Install Docker & Docker Compose
- ✓ Create project directories
- ✓ Upload files
- ✓ Generate SSL certificates
- ✓ Configure environment
- ✓ Build and start services
- ✓ Setup backups
- ✓ Configure auto-start
- ✓ Setup firewall

---

## Manual Deployment (45 minutes)

### Step 1: Connect to VPS

```bash
ssh root@YOUR_VPS_IP
```

### Step 2: Initial Setup

```bash
# Update system
apt update && apt upgrade -y

# Create non-root user
adduser deployer
usermod -aG sudo deployer

# Switch to new user
su - deployer
```

### Step 3: Install Docker

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# Add to docker group
sudo usermod -aG docker $USER
exit  # Logout and login
```

### Step 4: Install Docker Compose

```bash
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
  -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### Step 5: Upload Project Files

From your local machine:

```bash
ssh deployer@YOUR_VPS_IP "mkdir -p /opt/mssd/certs /opt/mssd/backups"
scp docker-compose.ssl.yml deployer@YOUR_VPS_IP:/opt/mssd/
scp nginx-ssl.conf deployer@YOUR_VPS_IP:/opt/mssd/
scp .env.production deployer@YOUR_VPS_IP:/opt/mssd/.env
scp -r mssd-backend deployer@YOUR_VPS_IP:/opt/mssd/
scp -r mssd-frontend deployer@YOUR_VPS_IP:/opt/mssd/
```

### Step 6: Configure Environment

On VPS:

```bash
cd /opt/mssd

# Edit .env with your domain and credentials
nano .env

# Update:
# MYSQL_ROOT_PASSWORD
# MYSQL_PASSWORD
# APP_DOMAIN=yourdomain.com
# JWT_SECRET (generate: openssl rand -base64 32)

chmod 600 .env
```

### Step 7: Generate SSL Certificate

Using Let's Encrypt:

```bash
sudo apt install -y certbot python3-certbot-nginx

sudo certbot certonly --standalone -d yourdomain.com -d www.yourdomain.com

sudo cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem /opt/mssd/certs/cert.pem
sudo cp /etc/letsencrypt/live/yourdomain.com/privkey.pem /opt/mssd/certs/key.pem
sudo chown $USER:$USER /opt/mssd/certs/*
```

### Step 8: Start Services

```bash
cd /opt/mssd

# Build images
docker-compose -f docker-compose.ssl.yml build

# Start services
docker-compose -f docker-compose.ssl.yml up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

### Step 9: Verify Deployment

```bash
# Check services are running
docker-compose ps

# Test frontend
curl https://yourdomain.com

# Test backend
curl https://yourdomain.com/api/health

# Check database
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SHOW DATABASES;"
```

### Step 10: Setup Backups

```bash
# Create backup script
cat > /opt/mssd/backup.sh << 'EOF'
#!/bin/bash
docker-compose -f docker-compose.ssl.yml exec -T mssd-db \
  mysqldump -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE | gzip > \
  /opt/mssd/backups/backup_$(date +%Y%m%d_%H%M%S).sql.gz
EOF

chmod +x /opt/mssd/backup.sh

# Schedule daily backup (2 AM)
(crontab -l 2>/dev/null; echo "0 2 * * * /opt/mssd/backup.sh") | crontab -
```

---

## Post-Deployment

### Access Your Application

- **Frontend:** https://yourdomain.com
- **Backend API:** https://yourdomain.com/api
- **Health Check:** https://yourdomain.com/api/health

### Monitor Services

```bash
# SSH to VPS
ssh deployer@YOUR_VPS_IP

# Check status
cd /opt/mssd
docker-compose ps

# View logs
docker-compose logs -f

# Real-time monitoring
docker stats
```

### Daily Operations

```bash
# View logs
docker-compose logs -f mssd-backend

# Check resources
docker stats

# Database backup
bash backup.sh

# Verify backups
ls -lh backups/

# Restart service
docker-compose restart mssd-backend
```

---

## Common Issues

### Services Won't Start

```bash
# Check Docker
sudo systemctl status docker

# Check logs
docker-compose logs

# Verify environment
cat .env
```

### Can't Access Frontend

```bash
# Check firewall
sudo ufw status

# Allow ports
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Check DNS
nslookup yourdomain.com
```

### Database Connection Error

```bash
# Check database is running
docker-compose ps mssd-db

# Check credentials in .env
cat .env | grep MYSQL

# Test connection
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SELECT 1"
```

### SSL Certificate Issues

```bash
# Check certificate expiration
openssl x509 -in certs/cert.pem -noout -dates

# Renew Let's Encrypt
sudo certbot renew --force-renewal
```

---

## Useful SSH Shortcuts

Add to `~/.bashrc`:

```bash
alias vps-ssh='ssh deployer@YOUR_VPS_IP'
alias vps-logs='ssh deployer@YOUR_VPS_IP "cd /opt/mssd && docker-compose logs -f"'
alias vps-ps='ssh deployer@YOUR_VPS_IP "cd /opt/mssd && docker-compose ps"'
alias vps-stats='ssh deployer@YOUR_VPS_IP "docker stats"'
```

---

## Maintenance Schedule

### Weekly
- [ ] Check logs for errors
- [ ] Verify backup files exist
- [ ] Monitor disk usage: `df -h`

### Monthly
- [ ] Review SSL certificate expiration
- [ ] Test database restore
- [ ] Update packages: `sudo apt update && sudo apt upgrade -y`

### Quarterly
- [ ] Full security audit
- [ ] Performance review
- [ ] Update Docker images

---

## Quick Reference

```bash
# Status
docker-compose ps

# Logs
docker-compose logs -f

# Backup
docker-compose exec mssd-db mysqldump -u root -p$MYSQL_ROOT_PASSWORD $MYSQL_DATABASE | gzip > backup.sql.gz

# Restore
gunzip < backup.sql.gz | docker-compose exec -T mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD $MYSQL_DATABASE

# Stop
docker-compose down

# Start
docker-compose -f docker-compose.ssl.yml up -d

# Resources
docker stats

# Disk
df -h

# Memory
free -h
```

---

## Next Steps

1. ✅ Deploy application
2. ✅ Configure DNS
3. ✅ Test application
4. ✅ Setup monitoring
5. ✅ Configure backups
6. ✅ Train team
7. ✅ Document procedures

---

## Support

- **VPS Guide:** Read `VPS-DEPLOYMENT-GUIDE.md`
- **Docker Guide:** Read `DOCKER-SETUP-GUIDE.md`
- **Troubleshooting:** Read `DOCKER-QUICK-REFERENCE.md`

---

**Deployed Successfully!** 🚀

Visit: https://yourdomain.com
