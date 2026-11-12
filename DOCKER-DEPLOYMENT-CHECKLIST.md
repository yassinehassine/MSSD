# Docker Deployment Checklist

Complete checklist for deploying MSSD using Docker containers.

---

## Pre-Deployment Phase

### Environment Preparation

- [ ] Docker installed and running (`docker --version`)
- [ ] Docker Compose installed (`docker-compose --version`)
- [ ] Sufficient disk space (minimum 20GB recommended)
- [ ] Sufficient RAM (minimum 4GB recommended)
- [ ] Network connectivity confirmed
- [ ] Firewall rules configured (ports 80, 443)

### Project Setup

- [ ] Repository cloned/extracted
- [ ] All Dockerfile files present in subdirectories
- [ ] `.env.production` template reviewed
- [ ] SSH keys/certificates ready (if applicable)
- [ ] Domain name configured with DNS

### Certificate Setup (SSL/TLS)

- [ ] SSL certificates obtained (Let's Encrypt or CA)
- [ ] Certificates copied to `./certs/` directory:
  - [ ] `cert.pem` (certificate file)
  - [ ] `key.pem` (private key)
- [ ] Certificate permissions set correctly:
  ```bash
  chmod 644 certs/cert.pem
  chmod 600 certs/key.pem
  ```
- [ ] Certificate validity verified:
  ```bash
  openssl x509 -in certs/cert.pem -text -noout
  ```

### Configuration Files

- [ ] `.env` file created from `.env.production`:
  ```bash
  cp .env.production .env
  ```
- [ ] All variables in `.env` updated:
  - [ ] `MYSQL_ROOT_PASSWORD` (strong password)
  - [ ] `MYSQL_USER` (non-default username)
  - [ ] `MYSQL_PASSWORD` (strong password)
  - [ ] `MYSQL_DATABASE` (production database name)
  - [ ] `APP_DOMAIN` (your domain)
  - [ ] `JWT_SECRET` (generated new secret)
  - [ ] `CORS_ALLOWED_ORIGINS` (your domain)
  - [ ] Mail credentials (if applicable)
- [ ] `.env` file permissions secured: `chmod 600 .env`
- [ ] `.env` file added to `.gitignore`
- [ ] No secrets committed to repository

### Scripts Preparation

- [ ] All scripts are executable:
  ```bash
  chmod +x scripts/*.sh
  ```
- [ ] Scripts reviewed for environment-specific values
- [ ] Backup directory created: `mkdir -p backups`

---

## Build Phase

### Image Building

- [ ] Backend image builds successfully:
  ```bash
  docker-compose build mssd-backend
  ```
- [ ] Frontend image builds successfully:
  ```bash
  docker-compose build mssd-frontend
  ```
- [ ] No build warnings or errors
- [ ] Image sizes are reasonable (< 500MB each)

### Build Verification

- [ ] Run build with no-cache:
  ```bash
  docker-compose build --no-cache
  ```
- [ ] All layers cached appropriately on subsequent builds
- [ ] Build artifacts cleaned up

### Container Testing

- [ ] Test backend container starts:
  ```bash
  docker-compose up -d mssd-db mssd-backend
  ```
- [ ] Backend container logs show no errors:
  ```bash
  docker-compose logs mssd-backend
  ```
- [ ] Backend health check passes:
  ```bash
  curl http://localhost:8082/actuator/health
  ```

---

## Deployment Phase

### Database Initialization

- [ ] Database volume created and verified
- [ ] Initial database dump loaded (if applicable)
- [ ] Database connection from backend confirmed
- [ ] All tables initialized successfully
- [ ] Sample data loaded (if applicable)

### Service Startup (Development)

```bash
# Start services
docker-compose up -d

# Verify all containers running
docker-compose ps

# Check logs
docker-compose logs -f

# Verify services
curl http://localhost/
curl http://localhost:8082/actuator/health
```

- [ ] All containers running without errors
- [ ] Database container healthy
- [ ] Backend container healthy
- [ ] Frontend container healthy
- [ ] No port conflicts
- [ ] No permission issues

### Service Startup (Production with SSL)

```bash
# Start services with SSL
docker-compose -f docker-compose.ssl.yml up -d

# Verify deployment
docker-compose -f docker-compose.ssl.yml ps

# Check Nginx configuration
docker-compose -f docker-compose.ssl.yml exec mssd-nginx nginx -t
```

- [ ] All containers running
- [ ] Nginx reverse proxy operational
- [ ] SSL certificates loaded correctly
- [ ] HTTP redirects to HTTPS
- [ ] API endpoints respond correctly

### Health Checks

- [ ] Frontend loads successfully:
  - [ ] Desktop: `https://yourdomain.com`
  - [ ] Mobile: Test on mobile device
- [ ] Backend API responds:
  ```bash
  curl https://yourdomain.com/api/health
  ```
- [ ] Database connection working:
  ```bash
  docker-compose exec mssd-backend curl -s http://localhost:8080/actuator/health
  ```
- [ ] All pages load without errors
- [ ] API endpoints return correct data

### SSL/TLS Verification

- [ ] HTTPS connection works
- [ ] SSL certificate valid:
  ```bash
  curl -v https://yourdomain.com
  ```
- [ ] No certificate warnings in browser
- [ ] Security headers present:
  ```bash
  curl -I https://yourdomain.com | grep X-
  ```
- [ ] HSTS header present

### Performance Check

- [ ] Page load times acceptable (< 3 seconds)
- [ ] API response times acceptable (< 1 second)
- [ ] No memory leaks (memory stable over time)
- [ ] CPU usage normal (not constantly high)
- [ ] Disk I/O normal
- [ ] Network latency acceptable

---

## Post-Deployment Phase

### Data Backup

- [ ] Initial database backup created:
  ```bash
  bash scripts/docker-backup.sh
  ```
- [ ] Backup file verified:
  ```bash
  ls -lh backups/
  ```
- [ ] Backup stored securely (off-site copy)
- [ ] Backup restoration tested

### Monitoring Setup

- [ ] Application logs accessible:
  ```bash
  docker-compose logs -f
  ```
- [ ] Backend Prometheus metrics accessible (if enabled):
  ```bash
  curl http://localhost:8082/actuator/prometheus
  ```
- [ ] Health check endpoint configured
- [ ] Alerts configured (if applicable)
- [ ] Monitoring dashboard accessible

### Scheduled Tasks

- [ ] Database backup scheduled (cron job):
  ```bash
  0 2 * * * cd /path/to/MSSD-mssd && bash scripts/docker-backup.sh
  ```
- [ ] Log rotation configured
- [ ] Certificate renewal scheduled (Let's Encrypt):
  ```bash
  0 3 * * * /path/to/renew-certs.sh
  ```

### Security Hardening

- [ ] Firewall configured (allow only 80, 443, SSH):
  ```bash
  # Example UFW commands
  ufw allow 22/tcp
  ufw allow 80/tcp
  ufw allow 443/tcp
  ufw enable
  ```
- [ ] SSH key-based authentication only
- [ ] Root login disabled
- [ ] Non-root user created for docker operations
- [ ] Docker socket access restricted
- [ ] Regular security updates scheduled

### Documentation

- [ ] Deployment procedure documented
- [ ] Access credentials recorded (securely)
- [ ] Backup/restore procedure documented
- [ ] Monitoring/alerting setup documented
- [ ] Disaster recovery plan created
- [ ] Team members trained

### Testing

- [ ] All frontend pages tested
- [ ] All API endpoints tested
- [ ] User authentication tested
- [ ] File uploads tested
- [ ] Email notifications tested (if applicable)
- [ ] Cross-browser testing completed:
  - [ ] Chrome
  - [ ] Firefox
  - [ ] Safari
  - [ ] Edge
- [ ] Mobile device testing completed
- [ ] Load testing completed (if required)

---

## Ongoing Maintenance

### Daily Tasks

- [ ] Monitor application logs for errors
- [ ] Check system resource usage
- [ ] Verify all services running
- [ ] Test critical functionality

### Weekly Tasks

- [ ] Review error logs
- [ ] Verify backup completion
- [ ] Check disk space
- [ ] Update security patches (if available)

### Monthly Tasks

- [ ] Full backup verification (restore test)
- [ ] Security audit
- [ ] Performance review
- [ ] Dependency updates check
- [ ] SSL certificate validity check (30 days before expiration)

### Quarterly Tasks

- [ ] Full system audit
- [ ] Capacity planning review
- [ ] Disaster recovery drill
- [ ] Documentation update

---

## Rollback Procedure

If deployment fails or issues arise:

### Quick Rollback

```bash
# Stop current deployment
docker-compose down

# Remove volumes (if needed)
docker volume rm VOLUME_NAME

# Restore from backup
gunzip < backups/BACKUP_FILE.sql.gz | \
  docker-compose exec -T mssd-db \
  mysql -u USER -pPASSWORD DATABASE

# Start previous version
docker-compose up -d
```

### Data Recovery

- [ ] Backup location verified
- [ ] Restore procedure tested
- [ ] Data integrity verified
- [ ] All tables present after restore

---

## Troubleshooting Quick Reference

| Issue | Solution |
|-------|----------|
| Port 80/443 already in use | `sudo lsof -i :80` and identify/stop process |
| Container won't start | Check logs: `docker-compose logs SERVICE` |
| Database connection error | Verify credentials in `.env` and network connectivity |
| SSL certificate error | Verify certificate paths and permissions |
| High memory usage | Check logs for leaks, restart service |
| API 502 error | Verify backend is running, check Nginx logs |
| Slow performance | Check resource usage, database queries, network |

---

## Contact & Support

For issues:
1. Check logs: `docker-compose logs -f`
2. Review this checklist
3. Consult DOCKER-SETUP-GUIDE.md
4. Contact support team with logs and error messages

---

**Deployment Date:** _______________
**Deployed By:** _______________
**Approved By:** _______________
**Notes:** _______________

---

**Version:** 1.0  
**Last Updated:** 2024
