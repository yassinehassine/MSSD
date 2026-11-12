# Docker Setup & Deployment Guide

Complete guide for containerizing, deploying, and maintaining the MSSD project using Docker.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Quick Start](#quick-start)
3. [Docker Compose Files](#docker-compose-files)
4. [SSL/TLS Configuration](#ssltls-configuration)
5. [Environment Variables](#environment-variables)
6. [Deployment Scenarios](#deployment-scenarios)
7. [Monitoring & Maintenance](#monitoring--maintenance)
8. [Troubleshooting](#troubleshooting)
9. [Security Best Practices](#security-best-practices)

---

## Prerequisites

### Required Software

- Docker 20.10+ ([Install Docker](https://docs.docker.com/engine/install/))
- Docker Compose 2.0+ ([Install Docker Compose](https://docs.docker.com/compose/install/))
- OpenSSL (for certificate generation)
- Bash shell (for setup scripts)

### System Requirements

- **Minimum**: 2GB RAM, 10GB disk space
- **Recommended**: 4GB RAM, 20GB disk space for production

### Knowledge Requirements

- Basic Docker concepts
- Terminal/CLI usage
- Environment variables

---

## Quick Start

### 1. Development Mode (HTTP)

```bash
# Clone or navigate to project root
cd MSSD-mssd

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

**Access Services:**
- Frontend: http://localhost
- Backend API: http://localhost:8082
- Database: localhost:3307
- phpMyAdmin: http://localhost:8081

### 2. Production Mode (HTTPS)

```bash
# Generate SSL certificates
bash scripts/generate-ssl-certs.sh yourdomain.com

# Update .env with production values
cp .env.production .env
# Edit .env with your domain and credentials

# Start with SSL configuration
docker-compose -f docker-compose.ssl.yml up -d

# View logs
docker-compose -f docker-compose.ssl.yml logs -f
```

**Access Services:**
- Frontend: https://yourdomain.com
- Backend API: https://yourdomain.com/api
- Database: Internal (not exposed)

---

## Docker Compose Files

### `docker-compose.yml` (Development)

**Features:**
- HTTP only (no SSL)
- phpMyAdmin included
- Database exposed on port 3307
- Backend exposed on port 8082
- Best for local development

**Services:**
```yaml
- mssd-db (MySQL 8.0)
- mssd-backend (Spring Boot)
- mssd-frontend (Angular + Nginx)
- phpmyadmin (optional)
```

**Usage:**
```bash
docker-compose up -d
```

---

### `docker-compose.ssl.yml` (Production with SSL)

**Features:**
- HTTPS/SSL support
- Nginx reverse proxy
- Prometheus monitoring (optional)
- Optimized for production
- Non-root user execution
- Enhanced security headers
- Logging configuration

**Services:**
```yaml
- mssd-db (MySQL 8.0)
- mssd-backend (Spring Boot)
- mssd-nginx (Nginx reverse proxy)
- mssd-frontend (Angular builder)
- prometheus (optional - add --profile monitoring)
- phpmyadmin (optional - add --profile debug)
```

**Usage:**
```bash
# Basic production deployment
docker-compose -f docker-compose.ssl.yml up -d

# With monitoring
docker-compose -f docker-compose.ssl.yml --profile monitoring up -d

# With debug tools
docker-compose -f docker-compose.ssl.yml --profile debug up -d
```

---

### `docker-compose.prod.yml` (Docker Swarm)

For distributed/clustered deployments using Docker Swarm mode:

```bash
# Initialize Docker Swarm
docker swarm init

# Deploy stack
docker stack deploy -c docker-compose.prod.yml mssd
```

---

## SSL/TLS Configuration

### Generate Self-Signed Certificates (Development/Testing)

```bash
# Generate certificate for 365 days
bash scripts/generate-ssl-certs.sh yourdomain.com 365

# Generated files:
# - certs/cert.pem (certificate)
# - certs/key.pem (private key)
```

### Use Let's Encrypt Certificates (Production)

For production with automatic HTTPS:

```bash
# 1. Create DNS records pointing to your server
# 2. Use Certbot to generate certificates

sudo certbot certonly --standalone -d yourdomain.com -d www.yourdomain.com

# 3. Copy certificates to certs directory
sudo cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem ./certs/cert.pem
sudo cp /etc/letsencrypt/live/yourdomain.com/privkey.pem ./certs/key.pem

# 4. Set permissions
chmod 644 certs/cert.pem
chmod 600 certs/key.pem

# 5. Start containers
docker-compose -f docker-compose.ssl.yml up -d
```

### Auto-Renewal with Let's Encrypt

```bash
# Create renewal script
cat > renew-certs.sh << 'EOF'
#!/bin/bash
certbot renew --quiet
cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem ./certs/cert.pem
cp /etc/letsencrypt/live/yourdomain.com/privkey.pem ./certs/key.pem
docker-compose -f docker-compose.ssl.yml restart mssd-nginx
EOF

chmod +x renew-certs.sh

# Add to crontab for daily renewal
(crontab -l 2>/dev/null; echo "0 3 * * * cd /path/to/MSSD-mssd && ./renew-certs.sh") | crontab -
```

---

## Environment Variables

### Development (.env)

```env
# Database
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=mssd_db
MYSQL_USER=mssd_user
MYSQL_PASSWORD=mssd_pass

# Backend
SPRING_PROFILES_ACTIVE=prod
APP_UPLOAD_DIR=/app/uploads

# Frontend
FRONTEND_PORT=80
```

### Production (.env.production)

```env
# Database (CHANGE THESE!)
MYSQL_ROOT_PASSWORD=ChangeMe_SuperStrong123!@#
MYSQL_DATABASE=mssd_production
MYSQL_USER=mssd_admin
MYSQL_PASSWORD=ChangeMe_AdminPass456!@#

# Application
SPRING_PROFILES_ACTIVE=prod
APP_UPLOAD_DIR=/app/uploads

# Frontend
FRONTEND_PORT=80
FRONTEND_API_BASE_URL=https://yourdomain.com/api

# Domain
APP_DOMAIN=yourdomain.com
APP_URL=https://yourdomain.com

# Email (Optional)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=noreply@yourdomain.com

# Security
JWT_SECRET=ChangeMe_GenerateNewSecret!@#$%^&*
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com

# File Upload
MAX_FILE_SIZE=10485760
MAX_REQUEST_SIZE=10485760
ALLOWED_FILE_TYPES=jpg,jpeg,png,gif,svg,webp

# Logging
LOG_LEVEL=INFO
LOG_FILE=/app/logs/application.log

# Performance
DB_POOL_SIZE=20
DB_MAX_IDLE_TIME=900
CACHE_ENABLED=true
CACHE_TTL=3600

# Monitoring
PROMETHEUS_ENABLED=true
METRICS_ENABLED=true
HEALTH_CHECK_ENABLED=true
```

### Generate Secure Credentials

```bash
# Generate strong password
openssl rand -base64 16

# Generate JWT secret
openssl rand -base64 32
```

---

## Deployment Scenarios

### Scenario 1: Local Development

```bash
# Setup
cd MSSD-mssd
docker-compose up -d

# Development workflow
docker-compose logs -f mssd-backend

# When done
docker-compose down
docker volume prune  # Clean up unused volumes (optional)
```

### Scenario 2: Staging Environment

```bash
# Setup
cp .env.production .env
# Edit .env with staging values

docker-compose -f docker-compose.ssl.yml up -d

# Backup database
bash scripts/docker-backup.sh

# Verify deployment
curl https://staging.yourdomain.com/health
```

### Scenario 3: Production Deployment

```bash
# Prerequisites
bash scripts/generate-ssl-certs.sh yourdomain.com

# Setup
cp .env.production .env
# Edit .env with production values, strong passwords
vim .env

# Deploy
docker-compose -f docker-compose.ssl.yml up -d

# Enable monitoring
docker-compose -f docker-compose.ssl.yml --profile monitoring up -d

# Backup
docker-compose -f docker-compose.ssl.yml exec mssd-db \
  mysqldump -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE > backup_$(date +%Y%m%d).sql
```

### Scenario 4: Docker Swarm Cluster

```bash
# Initialize Swarm
docker swarm init

# Deploy stack
docker stack deploy -c docker-compose.prod.yml mssd

# Scale services
docker service scale mssd_mssd-backend=3
docker service scale mssd_mssd-frontend=2

# View services
docker service ls
docker service ps mssd_mssd-backend

# Monitor
docker stats
```

---

## Monitoring & Maintenance

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f mssd-backend

# Last 100 lines
docker-compose logs --tail=100 mssd-backend

# With timestamps
docker-compose logs -f -t mssd-backend
```

### Database Backup

```bash
# Manual backup
bash scripts/docker-backup.sh

# Scheduled backup (crontab)
# Daily at 2 AM
0 2 * * * cd /path/to/MSSD-mssd && bash scripts/docker-backup.sh
```

### Database Restore

```bash
# Restore from backup
gunzip < backups/mysql_backup_20240101_020000.sql.gz | \
  docker-compose exec -T mssd-db \
  mysql -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE
```

### Health Checks

```bash
# Check service health
docker-compose ps

# Manual health check
curl http://localhost:8082/actuator/health
curl http://localhost/health

# Docker health status
docker inspect mssd-backend --format='{{json .State.Health}}'
```

### Monitoring with Prometheus

```bash
# Access Prometheus
http://localhost:9090

# Backend metrics
http://localhost:9090/targets

# View metrics
curl http://localhost:8082/actuator/prometheus
```

### Resource Usage

```bash
# Current usage
docker stats

# Container disk usage
docker exec mssd-backend du -sh /app/uploads

# Database size
docker-compose exec mssd-db \
  mysql -u root -p$MYSQL_ROOT_PASSWORD \
  -e "SELECT table_schema AS 'Database', ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size in MB' FROM information_schema.TABLES GROUP BY table_schema;"
```

---

## Troubleshooting

### Service Won't Start

```bash
# Check logs
docker-compose logs mssd-backend

# Verify network
docker network ls
docker network inspect mssd_mssd-network

# Check ports
netstat -tulpn | grep 8080

# Check disk space
df -h
```

### Database Connection Error

```bash
# Verify database is running
docker-compose ps mssd-db

# Test connection
docker-compose exec mssd-db \
  mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SELECT 1"

# Check credentials in .env
cat .env
```

### Frontend Not Loading

```bash
# Check frontend container
docker-compose logs mssd-frontend

# Verify Nginx configuration
docker-compose exec mssd-nginx nginx -t

# Check API proxy
curl -v http://localhost/api/health
```

### High Memory/CPU Usage

```bash
# Monitor resources
docker stats

# Check logs for errors
docker-compose logs mssd-backend | grep -i error

# Restart service
docker-compose restart mssd-backend

# Scale services (Swarm)
docker service scale mssd_mssd-backend=3
```

### SSL Certificate Issues

```bash
# Verify certificate
openssl x509 -in certs/cert.pem -text -noout

# Test SSL
curl -v https://yourdomain.com

# Restart Nginx
docker-compose -f docker-compose.ssl.yml restart mssd-nginx
```

---

## Security Best Practices

### 1. Environment Variables

```bash
# Never commit .env to Git
echo ".env" >> .gitignore

# Use strong passwords
openssl rand -base64 16

# Rotate credentials regularly
```

### 2. Database Security

```bash
# Don't expose database port in production
# Use only internal Docker network communication

# Run with minimal privileges
MYSQL_USER: read_write_user
MYSQL_PASSWORD: strong_password_123
```

### 3. SSL/TLS

```bash
# Use production certificates (Let's Encrypt)
# Enable HSTS headers
# Redirect HTTP to HTTPS
```

### 4. Container Security

```bash
# Run as non-root user
USER appuser

# Don't use privileged mode
# Use read-only filesystems where possible

# Scan images for vulnerabilities
docker scan mssd-backend:latest
```

### 5. Network Security

```bash
# Use internal Docker networks
# Expose only necessary ports
# Use reverse proxy (Nginx)
# Enable firewall rules
```

### 6. Monitoring & Logging

```bash
# Enable detailed logging
LOG_LEVEL=INFO

# Monitor all connections
# Set up alerts for suspicious activity
# Regularly review logs

# Keep audit trail
```

### 7. Backup & Disaster Recovery

```bash
# Regular database backups
# Off-site backup storage
# Test restore procedures
# Document recovery process

# Backup schedule: Daily at 2 AM
0 2 * * * cd /path/to/MSSD-mssd && bash scripts/docker-backup.sh
```

---

## Useful Docker Commands

```bash
# List all containers
docker ps -a

# View container details
docker inspect CONTAINER_ID

# Execute command in container
docker-compose exec SERVICE_NAME COMMAND

# View image layers
docker history IMAGE_NAME

# Clean up
docker system prune -a  # Remove unused images/containers/networks
docker volume prune      # Remove unused volumes

# Copy files
docker cp CONTAINER_ID:/path/to/file ./local/path
docker cp ./local/file CONTAINER_ID:/path/to/file

# View resource usage
docker stats

# Rebuild image
docker-compose build --no-cache SERVICE_NAME

# Update service (Swarm)
docker service update --image NEW_IMAGE SERVICE_NAME
```

---

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Documentation](https://spring.io/guides/gs/spring-boot-docker/)
- [Nginx Documentation](https://nginx.org/en/docs/)
- [MySQL Docker Image](https://hub.docker.com/_/mysql)
- [Let's Encrypt Certbot](https://certbot.eff.org/)

---

## Support & Questions

For issues or questions:

1. Check troubleshooting section
2. Review Docker/Docker Compose logs
3. Verify environment variables
4. Check firewall and network settings
5. Consult service-specific documentation

---

**Last Updated:** 2024
**Version:** 1.0
