# Docker Quick Reference Guide

Fast lookup for common Docker commands and operations for MSSD project.

---

## Start & Stop

### Development (HTTP)
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View running services
docker-compose ps

# View logs
docker-compose logs -f
```

### Production (HTTPS)
```bash
# Start with SSL
docker-compose -f docker-compose.ssl.yml up -d

# Stop services
docker-compose -f docker-compose.ssl.yml down

# View logs
docker-compose -f docker-compose.ssl.yml logs -f mssd-nginx
```

### With Profiles
```bash
# Development + Debug tools
docker-compose --profile debug up -d

# Production + Monitoring
docker-compose -f docker-compose.ssl.yml --profile monitoring up -d

# All services
docker-compose --profile debug --profile monitoring up -d
```

---

## Access Services

### Development
- **Frontend**: http://localhost
- **Backend**: http://localhost:8082
- **API Health**: http://localhost:8082/actuator/health
- **Database**: localhost:3307
- **phpMyAdmin**: http://localhost:8081

### Production
- **Frontend**: https://yourdomain.com
- **API**: https://yourdomain.com/api
- **Monitoring**: https://yourdomain.com:9090 (if enabled)

---

## View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f mssd-backend

# Last 100 lines
docker-compose logs --tail=100 mssd-backend

# With timestamps
docker-compose logs -f -t mssd-backend

# Backend only
docker-compose logs -f mssd-backend | grep ERROR

# Search for errors
docker-compose logs | grep "ERROR\|Exception"
```

---

## Execute Commands

```bash
# Execute in backend container
docker-compose exec mssd-backend java -version

# Execute in database
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e "SHOW DATABASES;"

# Execute in frontend
docker-compose exec mssd-frontend ls -la /usr/share/nginx/html

# Interactive shell
docker-compose exec mssd-backend /bin/sh
```

---

## Database Operations

### Backup
```bash
# Quick backup
docker-compose exec mssd-db mysqldump -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE > backup_$(date +%Y%m%d).sql

# Compressed backup
bash scripts/docker-backup.sh

# List backups
ls -lh backups/
```

### Restore
```bash
# Restore from SQL file
cat backup_20240101.sql | docker-compose exec -T mssd-db mysql -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE

# Restore from gzip
gunzip < backup_20240101.sql.gz | docker-compose exec -T mssd-db mysql -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE
```

### Query Database
```bash
# Connect to database
docker-compose exec mssd-db mysql -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE

# Execute query
docker-compose exec mssd-db mysql -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE -e "SELECT * FROM formations;"

# Check database size
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD -e \
  "SELECT table_schema, ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb FROM information_schema.TABLES GROUP BY table_schema;"
```

---

## Build & Rebuild

```bash
# Build images
docker-compose build

# Build without cache
docker-compose build --no-cache

# Build specific service
docker-compose build mssd-backend

# Build with optimized Dockerfile
docker build -f mssd-backend/Dockerfile.optimized -t mssd-backend:optimized ./mssd-backend
```

---

## Health Checks

```bash
# Check service status
docker-compose ps

# Backend health
curl http://localhost:8082/actuator/health

# Frontend health
curl http://localhost/health

# Database health
docker-compose exec mssd-db mysqladmin ping -u root -p$MYSQL_ROOT_PASSWORD

# Full health check
bash scripts/health-check-docker.sh
```

---

## Resource Usage

```bash
# Real-time resource usage
docker stats

# Stop stats updates
# Press Ctrl+C

# Specific container
docker stats mssd-backend

# Disk usage
docker system df

# Container disk usage
docker exec mssd-backend du -sh /app/uploads

# Database size
docker-compose exec mssd-db mysql -u root -p$MYSQL_ROOT_PASSWORD information_schema -e \
  "SELECT table_schema, ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb FROM TABLES GROUP BY table_schema;"
```

---

## Cleanup & Maintenance

```bash
# Remove stopped containers
docker-compose down

# Remove images
docker rmi mssd-backend mssd-frontend

# Remove volumes (WARNING: Deletes data!)
docker volume rm mssd-db-data mssd-uploads

# Full cleanup
docker system prune -a --volumes

# Remove dangling images
docker image prune -a

# Remove unused networks
docker network prune

# Check before cleanup
docker system df
```

---

## SSL/TLS Management

```bash
# Generate self-signed certificates
bash scripts/generate-ssl-certs.sh yourdomain.com 365

# Check certificate validity
openssl x509 -in certs/cert.pem -text -noout

# Verify certificate expiration
openssl x509 -in certs/cert.pem -noout -dates

# Test SSL connection
curl -v https://yourdomain.com

# Check security headers
curl -I https://yourdomain.com | grep X-
```

---

## Debugging

### Container Issues
```bash
# Inspect container
docker inspect CONTAINER_ID

# View container filesystem
docker diff CONTAINER_ID

# Copy files from container
docker cp CONTAINER_ID:/path/to/file ./local/path

# Copy files to container
docker cp ./local/file CONTAINER_ID:/path/to/file
```

### Network Issues
```bash
# Check network
docker network ls

# Inspect network
docker network inspect mssd-network

# Test container communication
docker-compose exec mssd-backend ping mssd-db

# Test DNS resolution
docker-compose exec mssd-backend nslookup mssd-db
```

### Log Analysis
```bash
# Search logs for errors
docker-compose logs | grep -i error

# Search logs for warnings
docker-compose logs | grep -i warning

# Count errors
docker-compose logs | grep -c "ERROR"

# Export logs to file
docker-compose logs > logs_backup_$(date +%Y%m%d).txt
```

---

## Image Management

```bash
# List images
docker images

# Image history
docker history mssd-backend:latest

# Search Docker Hub
docker search nginx

# Pull image
docker pull mysql:8.0

# Tag image
docker tag mssd-backend:latest mssd-backend:production

# Push image (to registry)
docker push registry.example.com/mssd-backend:latest

# Remove image
docker rmi IMAGE_ID

# Image size
docker images --format "table {{.Repository}}\t{{.Size}}"
```

---

## Secrets & Environment

```bash
# View environment variables in container
docker-compose exec mssd-backend env

# Check .env file
cat .env

# Update .env
nano .env  # or vim .env

# Verify variables loaded
docker-compose exec mssd-backend printenv | grep SPRING
```

---

## Container Information

```bash
# Get container ID
CONTAINER_ID=$(docker-compose ps -q mssd-backend)

# Get IP address
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' CONTAINER_ID

# Get container logs
docker logs CONTAINER_ID

# Get container processes
docker top CONTAINER_ID

# Get container statistics
docker stats CONTAINER_ID

# Get container metadata
docker inspect CONTAINER_ID | jq '.[] | .Mounts'
```

---

## Performance Optimization

```bash
# Check layer caching
docker history mssd-backend:latest

# Build optimization
docker build --cache-from mssd-backend:latest -t mssd-backend:new .

# Image size reduction
docker images --format "table {{.Repository}}\t{{.Size}}"

# Monitor during build
watch docker stats

# Scan for vulnerabilities
docker scan mssd-backend:latest
```

---

## Useful Aliases

```bash
# Add to ~/.bashrc or ~/.zshrc
alias dc='docker-compose'
alias dcup='docker-compose up -d'
alias dcdown='docker-compose down'
alias dclogs='docker-compose logs -f'
alias dcps='docker-compose ps'
alias dcexec='docker-compose exec'

# Usage: dc ps, dcup, dclogs, etc.
```

---

## Emergency Procedures

### Service Crashes
```bash
# Restart service
docker-compose restart mssd-backend

# Force restart
docker-compose kill mssd-backend
docker-compose up -d mssd-backend

# Restart all
docker-compose restart
```

### Out of Disk Space
```bash
# Check space
df -h

# Find large files
docker exec mssd-backend du -sh /app/uploads/*

# Clean old backups
find backups/ -mtime +30 -delete

# Cleanup Docker
docker system prune -a --volumes
```

### Database Locked
```bash
# Restart database
docker-compose restart mssd-db

# Wait for restart
sleep 10

# Check status
docker-compose logs mssd-db
```

### SSL Certificate Error
```bash
# Verify certificate
openssl x509 -in certs/cert.pem -text -noout

# Check permissions
ls -la certs/

# Restart Nginx
docker-compose -f docker-compose.ssl.yml restart mssd-nginx
```

---

## Common Issues & Solutions

| Problem | Solution |
|---------|----------|
| "Address already in use" | `docker-compose down` then `docker-compose up` |
| "Cannot connect to database" | Check credentials in `.env`, restart db: `docker-compose restart mssd-db` |
| "502 Bad Gateway" | Check backend logs: `docker-compose logs mssd-backend` |
| "Frontend blank page" | Check browser console, check Nginx logs |
| "High memory usage" | Check logs for errors, restart: `docker-compose restart mssd-backend` |
| "Disk space full" | Clean old logs/backups: `find . -name "*.log" -mtime +30 -delete` |

---

## External Resources

- [Docker Docs](https://docs.docker.com/)
- [Docker Compose Docs](https://docs.docker.com/compose/)
- [Docker CLI Reference](https://docs.docker.com/engine/reference/commandline/docker/)
- [MySQL Docker Image](https://hub.docker.com/_/mysql)
- [Nginx Docker Image](https://hub.docker.com/_/nginx)

---

**Tip:** Bookmark this page for quick access!  
**Last Updated:** 2024
