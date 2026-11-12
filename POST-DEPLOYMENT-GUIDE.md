# 📊 MSSD Post-Deployment Guide

## What to Do After Your Website Goes Live

---

## ✅ Immediate Actions (First Hour)

### 1. Verify All Systems

```bash
# SSH into your server
ssh root@your-server-ip

# Check all containers
docker-compose ps

# Check logs for errors
docker-compose logs --tail=50

# Test endpoints
curl https://yourdomain.com
curl https://yourdomain.com/api/actuator/health
curl https://yourdomain.com/api/formations
```

### 2. Test All Features

- [ ] **Homepage** loads correctly
- [ ] **Navigation** works
- [ ] **Forms** submit successfully
- [ ] **Images** load and display
- [ ] **API responses** are fast
- [ ] **Blog system** works
- [ ] **Contact form** sends emails
- [ ] **Mobile responsiveness** looks good

### 3. Monitor for Errors

```bash
# Watch logs in real-time
docker-compose logs -f

# Check system resources
docker stats

# Check disk space
df -h
```

---

## 🔒 Security Hardening (Day 1)

### 1. Update System

```bash
sudo apt update && sudo apt upgrade -y
sudo reboot
```

### 2. Install Firewall

```bash
# Install UFW firewall
sudo apt install ufw -y

# Allow SSH
sudo ufw allow 22/tcp

# Allow HTTP
sudo ufw allow 80/tcp

# Allow HTTPS
sudo ufw allow 443/tcp

# Block MySQL from outside
# (already internal only in Docker, but good practice)

# Enable firewall
sudo ufw enable

# Verify rules
sudo ufw status
```

### 3. Set Up Automatic Updates

```bash
# Install unattended-upgrades
sudo apt install unattended-upgrades -y

# Enable automatic updates
sudo dpkg-reconfigure -plow unattended-upgrades

# Check configuration
sudo cat /etc/apt/apt.conf.d/50unattended-upgrades
```

### 4. Secure SSH

```bash
# Edit SSH config
sudo nano /etc/ssh/sshd_config

# Make these changes:
# Port 22 (or change to custom port)
# PermitRootLogin no
# PubkeyAuthentication yes
# PasswordAuthentication no
# X11Forwarding no

# Restart SSH
sudo systemctl restart sshd
```

### 5. Configure Fail2Ban (Prevent Brute Force)

```bash
# Install Fail2Ban
sudo apt install fail2ban -y

# Start service
sudo systemctl start fail2ban
sudo systemctl enable fail2ban

# Create config
sudo nano /etc/fail2ban/jail.local
```

Add:
```ini
[DEFAULT]
bantime = 3600
findtime = 600
maxretry = 5

[sshd]
enabled = true
port = ssh

[nginx-http-auth]
enabled = true

[nginx-noscript]
enabled = true

[nginx-badbots]
enabled = true
```

Then:
```bash
sudo systemctl restart fail2ban
```

---

## 📊 Set Up Monitoring

### 1. Enable Application Monitoring

```bash
# Backend metrics are already exposed
curl https://yourdomain.com/api/actuator/prometheus

# Add to monitoring tools:
# - Prometheus
# - Grafana
# - New Relic
# - DataDog
# - etc.
```

### 2. Set Up Log Aggregation

```bash
# Centralize logs
docker-compose logs > /var/log/mssd/all-services.log

# Or use ELK Stack / Grafana Loki
```

### 3. Create Health Check Script

```bash
crontab -e
```

Add:
```cron
# Check health every 5 minutes
*/5 * * * * curl https://yourdomain.com/api/actuator/health || echo "Alert: Service Down" | mail admin@yourdomain.com
```

---

## 💾 Backup Strategy

### 1. Daily Database Backups

Already set up! Backups run daily at 2 AM.

### 2. Verify Backups Work

```bash
# List backups
ls -lh /opt/mssd-backups/

# Test restore process (in test environment)
docker exec -i mssd-db mysql -u root -p < /opt/mssd-backups/backup_latest.sql
```

### 3. Offsite Backup

```bash
# Copy to external storage weekly
aws s3 cp /opt/mssd-backups/ s3://your-backup-bucket/ --recursive

# Or use OVH Object Storage
# Or use Backblaze
```

### 4. Document Restore Procedure

```bash
# Create restore documentation
cat > /opt/RESTORE-PROCEDURE.md << 'EOF'
# How to Restore from Backup

1. Stop services
   docker-compose down

2. Restore database
   docker-compose up -d mssd-db
   sleep 10
   docker exec -i mssd-db mysql -u root -p < backup_file.sql

3. Restore uploads
   tar -xzf backup_uploads.tar.gz

4. Start all services
   docker-compose up -d

5. Verify
   curl https://yourdomain.com/api/actuator/health
EOF
```

---

## 📈 Performance Optimization

### 1. Enable Caching

```bash
# Update backend application-prod.properties
nano mssd-backend/src/main/resources/application-prod.properties

# Add cache configuration
spring.cache.type=simple
spring.cache.simple.spec=maximumSize=1000,expireAfterWrite=600s
```

### 2. Enable Nginx Caching

```bash
# Update nginx.conf to cache responses
nano mssd-frontend/nginx.conf

# Add caching headers
add_header Cache-Control "public, max-age=3600";
```

### 3. Enable GZIP Compression

Already enabled in nginx.conf, verify:

```bash
curl -I https://yourdomain.com | grep -i "content-encoding"
# Should show: content-encoding: gzip
```

### 4. Monitor Performance

```bash
# Check response times
curl -w "@curl-format.txt" -o /dev/null -s https://yourdomain.com

# Monitor docker resource usage
docker stats --no-stream

# Check database query performance
docker exec mssd-db mysql -u root -p -e "SHOW PROCESSLIST;"
```

---

## 📧 Email Configuration (Optional)

### 1. Set Up Email for Forms

```bash
# Update backend configuration
nano mssd-backend/src/main/resources/application-prod.properties

# Add mail configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 2. Test Email Sending

```bash
# Submit test contact form
curl -X POST https://yourdomain.com/api/contact \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "message": "Test message"
  }'

# Check if email received
```

---

## 🔄 Update & Maintenance Procedures

### 1. Deploy Code Updates

```bash
# Pull latest code
cd /opt/MSSD-mssd
git pull origin main

# Rebuild and restart
docker-compose down
docker-compose up -d --build

# Verify
docker-compose ps
docker-compose logs -f
```

### 2. Update Docker Images

```bash
# Update images
docker pull mysql:latest
docker pull node:latest
docker pull eclipse-temurin:latest

# Update services
docker-compose up -d --pull always

# Clean up old images
docker image prune -a
```

### 3. Database Maintenance

```bash
# Monthly database optimization
docker exec mssd-db mysql -u root -p -e "OPTIMIZE TABLE formations; OPTIMIZE TABLE portfolio; OPTIMIZE TABLE blog;"

# Check database size
docker exec mssd-db mysql -u root -p -e "SELECT table_name, ROUND(((data_length + index_length) / 1024 / 1024), 2) MB FROM information_schema.TABLES WHERE table_schema = 'mssd_production';"
```

---

## 📝 Logging & Monitoring

### 1. View Application Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f mssd-backend
docker-compose logs -f mssd-db
docker-compose logs -f mssd-frontend

# Last 100 lines
docker-compose logs --tail=100
```

### 2. Access Nginx Logs

```bash
# Access logs (who visited)
docker exec mssd-frontend tail -f /var/log/nginx/access.log

# Error logs
docker exec mssd-frontend tail -f /var/log/nginx/error.log
```

### 3. Enable Detailed Logging

```bash
# Update backend logging
nano mssd-backend/src/main/resources/application-prod.properties

# Set log level
logging.level.root=INFO
logging.level.com.mssd=DEBUG
logging.file.name=/app/logs/application.log
```

---

## 🚨 Alerting & Notifications

### 1. Set Up Status Page

```bash
# Using StatusPage.io, Uptime.com, or similar
# Automatically monitors your endpoints
```

### 2. Slack Notifications

```bash
# Install curl for notifications
sudo apt install curl -y

# Create notification script
cat > /usr/local/bin/notify-slack.sh << 'EOF'
#!/bin/bash
MESSAGE=$1
WEBHOOK_URL="https://hooks.slack.com/services/YOUR/WEBHOOK/URL"
curl -X POST -H 'Content-type: application/json' \
  --data "{\"text\":\"$MESSAGE\"}" \
  "$WEBHOOK_URL"
EOF

chmod +x /usr/local/bin/notify-slack.sh

# Add to crontab for alerts
```

### 3. Email Alerts

```bash
# Install mail utility
sudo apt install mailutils -y

# Configure mail alerts in crontab
*/30 * * * * if ! curl -s https://yourdomain.com > /dev/null; then echo "Website Down" | mail admin@yourdomain.com; fi
```

---

## 🎓 Team Training

### 1. Document Procedures

- [ ] How to deploy updates
- [ ] How to access logs
- [ ] How to restart services
- [ ] How to restore backups
- [ ] How to scale services
- [ ] How to handle emergencies

### 2. Create Runbooks

```bash
# Deployment runbook
cat > /opt/RUNBOOK.md << 'EOF'
# Operations Runbook

## Daily Tasks
- Check health: docker-compose ps
- Review logs: docker-compose logs --since 1h

## Weekly Tasks
- Verify backups: ls /opt/mssd-backups
- Performance check: docker stats

## Monthly Tasks
- Update system: apt update && apt upgrade
- Database optimization: OPTIMIZE TABLE...
- Security audit: fail2ban status
EOF
```

### 3. Create Escalation Contacts

- Primary contact: _______________
- Secondary contact: _______________
- Backup provider contact: _______________
- OVH support: https://www.ovh.com/support

---

## 📞 Regular Maintenance Calendar

### Daily
- [ ] Check health dashboard
- [ ] Review error logs
- [ ] Monitor uptime

### Weekly
- [ ] Verify backups created
- [ ] Check system updates available
- [ ] Review performance metrics
- [ ] Test backup restore (once)

### Monthly
- [ ] System security audit
- [ ] Database optimization
- [ ] Update Docker images
- [ ] Review disk usage
- [ ] Check SSL certificate expiration

### Quarterly
- [ ] Security hardening review
- [ ] Performance tuning
- [ ] Disaster recovery drill
- [ ] Update documentation

### Annually
- [ ] Major version upgrades
- [ ] Security penetration test
- [ ] Business continuity review
- [ ] Cost optimization review

---

## 🆘 Emergency Procedures

### If Site Goes Down

1. **Check immediately**
   ```bash
   docker-compose ps
   docker-compose logs --tail=50
   ```

2. **Restart services**
   ```bash
   docker-compose restart
   ```

3. **If database issue**
   ```bash
   docker-compose restart mssd-db
   ```

4. **Check disk space**
   ```bash
   df -h
   ```

5. **If all else fails, rollback**
   ```bash
   bash rollback.sh backup_20260803_123456
   ```

### If Hacked

1. **Immediately isolate database** (stop MySQL container)
2. **Restore from backup** (last known good state)
3. **Review logs** for compromise evidence
4. **Rotate all passwords**
5. **Update security patches**
6. **Implement additional security** (WAF, etc.)

---

## 🎯 Success Metrics to Monitor

- **Uptime**: Target 99.9%
- **Response Time**: < 500ms
- **Database Queries**: < 100ms
- **Error Rate**: < 0.1%
- **API Availability**: 99.9%
- **Backup Success Rate**: 100%
- **Security Incidents**: 0

---

## 📚 Additional Resources

- **OVH Documentation**: https://docs.ovh.com/
- **Docker Best Practices**: https://docs.docker.com/
- **Spring Boot**: https://spring.io/projects/spring-boot
- **Angular**: https://angular.io/docs
- **OWASP Security**: https://owasp.org/

---

## ✅ Post-Deployment Completion Checklist

- [ ] All services running and healthy
- [ ] SSL certificate installed and valid
- [ ] Backups verified and automated
- [ ] Monitoring configured and alerting
- [ ] Firewall rules configured
- [ ] Automatic updates enabled
- [ ] Security hardening completed
- [ ] Team trained on procedures
- [ ] Disaster recovery plan documented
- [ ] Performance baseline established
- [ ] Logs aggregation set up
- [ ] Maintenance schedule created

---

## 🎉 Congratulations!

Your MSSD website is **production-ready** and **well-maintained**!

Monitor regularly, update consistently, and your system will run smoothly.

---

**Last Updated**: August 2026  
**Version**: 1.0  
**Status**: Ready for Production ✅
