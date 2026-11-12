# 🚀 OVH Cloud Deployment Guide for MSSD Website

## 📋 Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Deployment Options](#deployment-options)
4. [Option 1: Docker Deployment (Recommended)](#option-1-docker-deployment-recommended)
5. [Option 2: Manual Deployment](#option-2-manual-deployment)
6. [Database Setup](#database-setup)
7. [Domain Configuration](#domain-configuration)
8. [SSL Certificate Setup](#ssl-certificate-setup)
9. [Monitoring & Maintenance](#monitoring--maintenance)
10. [Troubleshooting](#troubleshooting)

---

## 📖 Overview

This guide will help you deploy the MSSD website to OVH Cloud. Your application consists of:

- **Frontend**: Angular 20 (Nginx server)
- **Backend**: Spring Boot (Java 17) REST API
- **Database**: MySQL 8.0
- **File Storage**: Uploaded images and files

**Architecture:**
```
Internet → OVH Load Balancer/Reverse Proxy → Frontend (Port 80)
                                            → Backend (Port 8080)
                                            → MySQL Database (Port 3306)
```

---

## ✅ Prerequisites

### What You Need from OVH

1. **VPS/Dedicated Server** (Recommended minimum specs):
   - **CPU**: 2 vCores
   - **RAM**: 4GB
   - **Storage**: 40GB SSD
   - **OS**: Ubuntu 22.04 LTS or Debian 11

2. **Domain Name** (from OVH or transferred to OVH)

3. **OVH Control Panel Access**

### Software Required on Server

- Docker & Docker Compose (Option 1)
- OR: Java 17, Node.js 20, MySQL 8.0, Nginx (Option 2)

---

## 🎯 Deployment Options

### Option 1: Docker Deployment (⭐ Recommended)
- **Pros**: Easy setup, consistent environment, easy updates
- **Cons**: Requires Docker knowledge
- **Time**: 30-45 minutes

### Option 2: Manual Deployment
- **Pros**: Full control, optimized performance
- **Cons**: More complex setup, harder to maintain
- **Time**: 2-3 hours

---

## 🐳 Option 1: Docker Deployment (Recommended)

### Step 1: Connect to Your OVH Server

```bash
# From your local machine
ssh root@your-server-ip

# Or use OVH KVM console from control panel
```

### Step 2: Install Docker & Docker Compose

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install Docker Compose
sudo apt install docker-compose -y

# Verify installation
docker --version
docker-compose --version

# Start Docker service
sudo systemctl start docker
sudo systemctl enable docker
```

### Step 3: Upload Your Application

**Option A: Using Git (Recommended)**

```bash
# Install Git
sudo apt install git -y

# Clone your repository
cd /opt
git clone https://github.com/yourusername/MSSD-mssd.git
cd MSSD-mssd

# Or if private repo
git clone https://username:token@github.com/yourusername/MSSD-mssd.git
```

**Option B: Using SCP/SFTP**

From your local machine:
```bash
# Compress your project
cd c:\Users\souha\Downloads\MSSD-mssd
tar -czf mssd.tar.gz MSSD-mssd/

# Upload to server
scp mssd.tar.gz root@your-server-ip:/opt/

# On server, extract
cd /opt
tar -xzf mssd.tar.gz
cd MSSD-mssd
```

### Step 4: Configure Environment Variables

```bash
# Edit .env file
nano .env
```

Update the following values:

```env
# ============================================
# MSSD Production Environment
# ============================================

# Database (CHANGE THESE!)
MYSQL_ROOT_PASSWORD=YOUR_STRONG_PASSWORD_HERE
MYSQL_DATABASE=mssd_production
MYSQL_USER=mssd_admin
MYSQL_PASSWORD=YOUR_STRONG_DB_PASSWORD_HERE

# Backend
SPRING_PROFILES_ACTIVE=prod
APP_UPLOAD_DIR=/app/uploads

# Frontend
FRONTEND_PORT=80
```

**⚠️ Security Note**: Use strong passwords! Generate with:
```bash
openssl rand -base64 32
```

### Step 5: Update Backend Configuration

Create production configuration:

```bash
nano mssd-backend/src/main/resources/application-prod.properties
```

Add:

```properties
# Server Configuration
server.port=8080
server.compression.enabled=true

# Database - Use Docker service name
spring.datasource.url=jdbc:mysql://mssd-db:3306/${MYSQL_DATABASE}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
app.upload.dir=/app/uploads

# CORS - Update with your domain
spring.web.cors.allowed-origins=https://yourdomain.com,https://www.yourdomain.com
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*

# Actuator (Health checks)
management.endpoints.web.exposure.include=health,prometheus,info
management.endpoint.health.show-details=always
```

### Step 6: Update Frontend API URL

```bash
# Edit the frontend environment file
nano mssd-frontend/src/environments/environment.prod.ts
```

If this file doesn't exist, create it:

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.yourdomain.com/api'
  // Or if backend is on same domain: '/api'
};
```

### Step 7: Update Nginx Configuration

```bash
nano mssd-frontend/nginx.conf
```

Update the backend proxy:

```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    root /usr/share/nginx/html;
    index index.html;

    # Gzip compression
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;

    # Frontend routes
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API Proxy to Backend
    location /api/ {
        proxy_pass http://mssd-backend:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Static files caching
    location ~* \.(jpg|jpeg|png|gif|ico|css|js|svg|woff|woff2|ttf)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### Step 8: Build and Start Services

```bash
# Make sure you're in the project root
cd /opt/MSSD-mssd

# Build and start all services
docker-compose up -d --build

# This will:
# 1. Build backend Docker image
# 2. Build frontend Docker image
# 3. Pull MySQL image
# 4. Start all services
```

### Step 9: Monitor Deployment

```bash
# Check container status
docker-compose ps

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f mssd-backend
docker-compose logs -f mssd-frontend
docker-compose logs -f mssd-db

# Check backend health
curl http://localhost:8082/actuator/health

# Check frontend
curl http://localhost:80
```

### Step 10: Verify Everything Works

```bash
# Test backend API
curl http://localhost:8082/api/formations

# Test frontend
curl http://localhost:80

# Check database
docker exec -it mssd-db mysql -u mssd_user -p
# Enter password and run:
SHOW DATABASES;
USE mssd_db;
SHOW TABLES;
EXIT;
```

---

## 🔧 Option 2: Manual Deployment

### Step 1: Install Prerequisites

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y
java -version

# Install MySQL
sudo apt install mysql-server -y
sudo systemctl start mysql
sudo systemctl enable mysql

# Install Node.js 20
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install nodejs -y
node --version
npm --version

# Install Nginx
sudo apt install nginx -y
sudo systemctl start nginx
sudo systemctl enable nginx

# Install Maven
sudo apt install maven -y
mvn --version
```

### Step 2: Setup MySQL Database

```bash
# Secure MySQL installation
sudo mysql_secure_installation

# Login to MySQL
sudo mysql -u root -p

# Create database and user
CREATE DATABASE mssd_production CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'mssd_admin'@'localhost' IDENTIFIED BY 'YOUR_STRONG_PASSWORD';
GRANT ALL PRIVILEGES ON mssd_production.* TO 'mssd_admin'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Step 3: Deploy Backend

```bash
# Create application directory
sudo mkdir -p /opt/mssd
cd /opt/mssd

# Upload and extract your backend
# (Use scp/sftp to upload mssd-backend folder)

# Configure application.properties
cd mssd-backend
nano src/main/resources/application-prod.properties
```

Add:
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mssd_production
spring.datasource.username=mssd_admin
spring.datasource.password=YOUR_STRONG_PASSWORD
spring.jpa.hibernate.ddl-auto=update
app.upload.dir=/opt/mssd/uploads
```

Build and run:
```bash
# Build application
mvn clean package -DskipTests

# Create uploads directory
sudo mkdir -p /opt/mssd/uploads
sudo chmod 755 /opt/mssd/uploads

# Run as background service
nohup java -jar target/mssd-backend-1.0.0.jar \
  --spring.profiles.active=prod > backend.log 2>&1 &

# Check logs
tail -f backend.log
```

### Step 4: Deploy Frontend

```bash
cd /opt/mssd/mssd-frontend

# Install dependencies
npm ci

# Update API URL in environment
nano src/environments/environment.prod.ts

# Build for production
npm run build -- --configuration=production

# Copy built files to Nginx
sudo cp -r dist/mssd-app/browser/* /var/www/html/mssd/
```

### Step 5: Configure Nginx

```bash
sudo nano /etc/nginx/sites-available/mssd
```

Add:
```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    root /var/www/html/mssd;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable site:
```bash
sudo ln -s /etc/nginx/sites-available/mssd /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Step 6: Create Systemd Services

**Backend Service:**
```bash
sudo nano /etc/systemd/system/mssd-backend.service
```

Add:
```ini
[Unit]
Description=MSSD Backend Service
After=network.target mysql.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/mssd/mssd-backend
ExecStart=/usr/bin/java -jar /opt/mssd/mssd-backend/target/mssd-backend-1.0.0.jar --spring.profiles.active=prod
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start:
```bash
sudo systemctl daemon-reload
sudo systemctl enable mssd-backend
sudo systemctl start mssd-backend
sudo systemctl status mssd-backend
```

---

## 🗄️ Database Setup

### Initial Data Import

If you have existing data to import:

```bash
# Export from local database
mysqldump -u root -p mssd_db > mssd_backup.sql

# Upload to server
scp mssd_backup.sql root@your-server-ip:/tmp/

# Import on server
mysql -u mssd_admin -p mssd_production < /tmp/mssd_backup.sql
```

### Database Backup Script

```bash
# Create backup script
sudo nano /opt/mssd/backup-db.sh
```

Add:
```bash
#!/bin/bash
BACKUP_DIR="/opt/mssd/backups"
DATE=$(date +%Y%m%d_%H%M%S)
mkdir -p $BACKUP_DIR

# Docker deployment
docker exec mssd-db mysqldump -u mssd_admin -pYOUR_PASSWORD mssd_production > $BACKUP_DIR/backup_$DATE.sql

# Manual deployment
# mysqldump -u mssd_admin -p mssd_production > $BACKUP_DIR/backup_$DATE.sql

# Keep only last 7 days
find $BACKUP_DIR -name "backup_*.sql" -mtime +7 -delete

echo "Backup completed: backup_$DATE.sql"
```

Make executable and schedule:
```bash
chmod +x /opt/mssd/backup-db.sh

# Add to crontab (daily at 2 AM)
crontab -e
# Add line:
0 2 * * * /opt/mssd/backup-db.sh
```

---

## 🌐 Domain Configuration

### Step 1: Point Domain to Server

In OVH Control Panel:

1. Go to **Domains** → Select your domain
2. Click **DNS Zone**
3. Add/Edit records:

```
Type    Subdomain    Target              TTL
A       @            YOUR_SERVER_IP      3600
A       www          YOUR_SERVER_IP      3600
CNAME   api          yourdomain.com      3600
```

### Step 2: Wait for DNS Propagation

```bash
# Check DNS propagation (5-60 minutes)
nslookup yourdomain.com
dig yourdomain.com
```

---

## 🔒 SSL Certificate Setup

### Using Let's Encrypt (Free)

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtain certificate
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com

# Follow prompts and choose:
# - Redirect HTTP to HTTPS: Yes

# Auto-renewal is configured automatically
# Test renewal:
sudo certbot renew --dry-run
```

### Manual Nginx SSL Configuration

If you have a certificate from OVH or another provider:

```bash
sudo nano /etc/nginx/sites-available/mssd
```

Update:
```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;
    
    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    
    root /usr/share/nginx/html;
    index index.html;
    
    # Rest of configuration...
}
```

---

## 📊 Monitoring & Maintenance

### Health Checks

```bash
# Backend health
curl https://yourdomain.com/api/actuator/health

# Database connection
docker exec -it mssd-db mysqladmin ping -u root -p

# Disk space
df -h

# Memory usage
free -h

# Docker container status
docker stats
```

### Log Management

```bash
# Docker logs
docker-compose logs -f --tail=100

# Application logs
tail -f /opt/mssd/mssd-backend/backend.log

# Nginx logs
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### Monitoring Dashboard

Install Grafana + Prometheus (optional):

```bash
cd /opt/MSSD-mssd

# Start monitoring stack
docker-compose -f docker-compose.monitoring.yml up -d

# Access:
# Grafana: http://your-server-ip:3000 (admin/admin)
# Prometheus: http://your-server-ip:9090
```

---

## 🔄 Updating Your Application

### Docker Deployment

```bash
cd /opt/MSSD-mssd

# Pull latest code
git pull origin main

# Rebuild and restart
docker-compose down
docker-compose up -d --build

# Or update specific service
docker-compose up -d --build mssd-frontend
```

### Manual Deployment

**Backend:**
```bash
cd /opt/mssd/mssd-backend
git pull
mvn clean package -DskipTests
sudo systemctl restart mssd-backend
```

**Frontend:**
```bash
cd /opt/mssd/mssd-frontend
git pull
npm run build -- --configuration=production
sudo cp -r dist/mssd-app/browser/* /var/www/html/mssd/
```

---

## 🐛 Troubleshooting

### Issue: Backend Won't Start

```bash
# Check Java version
java -version  # Should be 17

# Check database connection
docker exec -it mssd-db mysql -u mssd_admin -p

# Check backend logs
docker logs mssd-backend -f

# Check port availability
netstat -tulpn | grep 8080
```

### Issue: Frontend Shows 404 Errors

```bash
# Check Nginx configuration
sudo nginx -t

# Check Nginx logs
sudo tail -f /var/log/nginx/error.log

# Verify files exist
ls -la /usr/share/nginx/html/
```

### Issue: Database Connection Failed

```bash
# Check MySQL is running
docker ps | grep mssd-db
sudo systemctl status mysql

# Test connection
docker exec -it mssd-db mysql -u root -p

# Check credentials in .env file
cat .env
```

### Issue: SSL Certificate Problems

```bash
# Check certificate validity
sudo certbot certificates

# Force renewal
sudo certbot renew --force-renewal

# Check Nginx SSL configuration
sudo nginx -t
```

### Issue: Out of Disk Space

```bash
# Check disk usage
df -h

# Clean Docker images
docker system prune -a

# Clean old backups
find /opt/mssd/backups -name "*.sql" -mtime +30 -delete

# Clean logs
sudo truncate -s 0 /var/log/nginx/access.log
```

---

## 📞 OVH Support Resources

- **OVH Manager**: https://www.ovh.com/manager/
- **OVH Documentation**: https://docs.ovh.com/
- **Support Ticket**: Create from OVH Manager
- **Community**: https://community.ovh.com/

---

## ✅ Post-Deployment Checklist

- [ ] Server is accessible via SSH
- [ ] Docker/Services are running
- [ ] Database is created and accessible
- [ ] Backend API responds to health checks
- [ ] Frontend loads in browser
- [ ] Domain points to server
- [ ] SSL certificate is installed and valid
- [ ] API calls work from frontend to backend
- [ ] File uploads work (test blog image upload)
- [ ] Database backups are scheduled
- [ ] Monitoring is set up (optional)
- [ ] Firewall rules are configured
- [ ] Server auto-updates are enabled

---

## 🎉 Success!

Your MSSD website should now be live at **https://yourdomain.com**

### Test All Features:
1. **Homepage**: https://yourdomain.com
2. **Formations**: https://yourdomain.com/services
3. **Portfolio**: https://yourdomain.com/portfolio
4. **Blog**: https://yourdomain.com/blog
5. **Contact Form**: https://yourdomain.com/contact
6. **Backend API**: https://yourdomain.com/api/actuator/health

---

## 📈 Next Steps

1. **Performance Optimization**:
   - Enable Redis caching
   - Configure CDN (OVH CDN or Cloudflare)
   - Optimize database queries

2. **Security Hardening**:
   - Set up fail2ban
   - Configure firewall (ufw)
   - Enable security headers
   - Regular security updates

3. **Scalability**:
   - Set up load balancer
   - Add more backend instances
   - Database replication

---

**Need Help?** Create an issue in your repository or contact OVH support.

**Version**: 1.0  
**Last Updated**: August 2026  
**Author**: MSSD Team
