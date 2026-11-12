# 🚀 MSSD OVH Deployment - Quick Reference

## One-Page Deployment Summary

---

## 📋 Prerequisites

- OVH VPS/Dedicated (2vCPU, 4GB RAM, 40GB SSD)
- Domain name pointing to server IP
- SSH access to server
- Estimated time: 30-45 minutes

---

## 🚀 Quick Start (5 Steps)

### Step 1: Connect to Server
```bash
ssh root@your-server-ip
```

### Step 2: Clone Project
```bash
cd /opt
git clone https://github.com/yourusername/MSSD-mssd.git
cd MSSD-mssd
```

### Step 3: Configure Environment
```bash
# Edit .env with production values
nano .env

# Key values to change:
# - MYSQL_ROOT_PASSWORD (strong password)
# - MYSQL_PASSWORD (strong password)
# - Domain settings
```

### Step 4: Run Installation
```bash
# Make scripts executable
chmod +x *.sh

# Run quick start
sudo bash quick-start-ovh.sh
```

### Step 5: Configure SSL
```bash
# Install certificate
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d yourdomain.com
```

---

## ✅ Verify Deployment

```bash
# Check services
docker-compose ps

# Test backend
curl https://yourdomain.com/api/actuator/health

# Test frontend
curl https://yourdomain.com
```

---

## 🐛 Common Issues

| Issue | Solution |
|-------|----------|
| Port already in use | `netstat -tulpn \| grep :80` then kill process |
| Database connection failed | Check MySQL is running: `docker ps \| grep mysql` |
| Out of memory | Increase server RAM or enable swap |
| Certificate errors | Verify domain DNS is correct |
| Slow response | Check server resources: `docker stats` |

---

## 📊 Important Commands

### Monitor Services
```bash
docker-compose logs -f           # Real-time logs
docker-compose ps               # Service status
docker stats                     # Resource usage
```

### Manage Database
```bash
docker exec -it mssd-db mysql -u root -p
SHOW DATABASES;
SHOW TABLES;
SELECT COUNT(*) FROM formations;
```

### Restart Services
```bash
docker-compose restart mssd-backend
docker-compose restart mssd-frontend
docker-compose restart mssd-db
```

### View Backups
```bash
ls -lh /opt/mssd-backups/
```

### Rollback Deployment
```bash
bash rollback.sh backup_20260803_123456
```

---

## 🔒 Security Checklist

- [ ] Change database passwords
- [ ] Enable SSL certificate
- [ ] Configure firewall (ufw)
- [ ] Install fail2ban
- [ ] Enable automatic updates
- [ ] Configure SSH key auth
- [ ] Set up backups

---

## 📈 Performance Tips

1. **Enable Caching**: Update `application-prod.properties`
2. **Enable Compression**: Already in `nginx.conf`
3. **Optimize Queries**: Add database indexes
4. **Use CDN**: For static assets
5. **Monitor Regularly**: Use `docker stats`

---

## 🔄 Update Deployment

```bash
cd /opt/MSSD-mssd
git pull origin main
docker-compose down
docker-compose up -d --build
```

---

## 📞 Quick Support

| Item | Solution |
|------|----------|
| **Can't SSH** | Use OVH KVM console |
| **Domain not working** | Check DNS in OVH Manager |
| **Services won't start** | Check `docker-compose logs` |
| **Database error** | Verify MySQL credentials in `.env` |
| **SSL not working** | Run certbot again |

---

## 🎯 Health Check URLs

After deployment:

| URL | Expected |
|-----|----------|
| `https://yourdomain.com` | Frontend loads |
| `https://yourdomain.com/api/actuator/health` | `{"status":"UP"}` |
| `https://yourdomain.com/api/formations` | JSON array |

---

## 📊 Monitoring Dashboard

Access health info:

```bash
# Backend metrics
curl https://yourdomain.com/api/actuator/prometheus

# Database info
docker exec mssd-db mysql -u root -p -e "SELECT VERSION();"

# Docker stats
docker stats --no-stream
```

---

## 💾 Backup & Restore

**Automatic backups run daily at 2 AM**

Restore from backup:
```bash
bash rollback.sh backup_20260803_123456
```

---

## 🚨 Emergency Restart

If everything stops:

```bash
docker-compose down
docker-compose up -d

# Then verify
docker-compose logs -f
```

---

## 📈 Scaling

To handle more users:

1. **Add backend instances**
2. **Increase database connections**
3. **Enable Redis caching**
4. **Use load balancer**
5. **Add CDN**

---

## 📝 Helpful Files

| File | Purpose |
|------|---------|
| `OVH-DEPLOYMENT-GUIDE.md` | Detailed guide |
| `PRE-DEPLOYMENT-CHECKLIST.md` | Before deployment |
| `POST-DEPLOYMENT-GUIDE.md` | After deployment |
| `quick-start-ovh.sh` | Auto setup |
| `deploy-to-ovh.sh` | Auto deployment |
| `health-check.sh` | System check |
| `rollback.sh` | Restore backup |

---

## 🎉 Success Indicators

- ✅ Services running: `docker-compose ps` shows all UP
- ✅ Website accessible: `curl https://yourdomain.com` returns 200
- ✅ Backend working: API returns health status
- ✅ Database connected: Can query tables
- ✅ SSL valid: Browser shows green lock
- ✅ Backups running: Files in `/opt/mssd-backups/`

---

## 📞 Getting Help

1. **Check Documentation**: See `OVH-DEPLOYMENT-GUIDE.md`
2. **Review Logs**: `docker-compose logs -f`
3. **Run Health Check**: `bash health-check.sh`
4. **Contact OVH**: Create support ticket
5. **Check Updates**: `git pull origin main`

---

## 🔐 Password Security

Don't forget to change these in `.env`:

```
MYSQL_ROOT_PASSWORD=ChangeMe_123!@#
MYSQL_PASSWORD=ChangeMe_456!@#
JWT_SECRET=ChangeMe_NewSecret
```

Generate strong passwords:
```bash
openssl rand -base64 32
```

---

**Version**: 1.0  
**Last Updated**: August 2026  
**Status**: Production Ready ✅

🚀 **You're ready to deploy!**
