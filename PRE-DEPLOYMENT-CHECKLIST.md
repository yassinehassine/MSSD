# ✅ MSSD OVH Pre-Deployment Checklist

## 📋 Before You Deploy

Use this checklist to ensure everything is ready for deployment.

---

## 🖥️ OVH Server Setup

- [ ] **Server Purchased & Activated**
  - Check OVH Manager for server status
  - Note your server IP address: `_______________`

- [ ] **Server Access Verified**
  - [ ] Can SSH into server: `ssh root@your-ip`
  - [ ] Root password saved securely
  - [ ] KVM console accessible (backup access method)

- [ ] **Server Initial Configuration**
  - [ ] System updated: `apt update && apt upgrade -y`
  - [ ] Timezone set correctly
  - [ ] Hostname configured
  - [ ] Firewall configured (optional but recommended)

---

## 🌐 Domain Configuration

- [ ] **Domain Name Acquired**
  - Domain name: `_______________`
  - Domain registrar: `_______________`

- [ ] **Domain Transferred to OVH (if applicable)**
  - [ ] Transfer initiated
  - [ ] Authorization codes obtained
  - [ ] Transfer completed

- [ ] **DNS Records Ready**
  - [ ] DNS zone created in OVH Manager
  - [ ] A record pointing to server IP
  - [ ] WWW record pointing to server IP
  - Test with: `nslookup yourdomain.com`

---

## 📦 Application Preparation

### Backend
- [ ] **Java Configuration**
  - [ ] Java 17 installed locally (for building JAR)
  - [ ] Maven installed locally (for building)
  - [ ] `pom.xml` is complete and correct
  - [ ] All dependencies resolved

- [ ] **Production Properties**
  - [ ] `application-prod.properties` created
  - [ ] Database URL configured
  - [ ] CORS origins set correctly
  - [ ] Upload directory path configured
  - [ ] Logging configured

- [ ] **Dockerfile**
  - [ ] Backend Dockerfile exists
  - [ ] Multi-stage build configured
  - [ ] Health checks configured
  - [ ] Ports exposed correctly

### Frontend
- [ ] **Angular Build**
  - [ ] `package.json` has build script
  - [ ] `angular.json` configured for production
  - [ ] Build completes successfully locally: `npm run build`
  - [ ] Built files in `dist/mssd-app/browser/`

- [ ] **Environment Configuration**
  - [ ] `environment.prod.ts` created
  - [ ] API URL points to correct backend
  - [ ] API URL uses HTTPS in production

- [ ] **Nginx Configuration**
  - [ ] `nginx.conf` exists
  - [ ] API proxy configured to backend
  - [ ] Static file caching configured
  - [ ] Gzip compression enabled

- [ ] **Frontend Dockerfile**
  - [ ] Multi-stage build (Node + Nginx)
  - [ ] Production build enabled
  - [ ] Health checks configured

### Database
- [ ] **Database Schema**
  - [ ] All migrations created
  - [ ] Tables defined in entities
  - [ ] Indexes added for performance
  - [ ] Relationships configured

- [ ] **Data Preparation**
  - [ ] Existing data exported (if applicable)
  - [ ] Backup file created: `_______________`
  - [ ] Data import script tested

---

## 🐳 Docker Configuration

- [ ] **docker-compose.yml**
  - [ ] All services defined (frontend, backend, db)
  - [ ] Environment variables referenced
  - [ ] Volumes configured
  - [ ] Networks configured
  - [ ] Health checks added
  - [ ] Dependencies configured

- [ ] **Production .env file**
  - [ ] Copy of `.env.production` created
  - [ ] All passwords changed from defaults
  - [ ] Domain name updated
  - [ ] Email configuration added (if needed)

- [ ] **Images Built Locally**
  - [ ] Backend image builds successfully
  - [ ] Frontend image builds successfully
  - [ ] No build errors or warnings

---

## 🔒 Security Preparation

- [ ] **Passwords & Credentials**
  - [ ] MySQL root password generated (32+ chars)
  - [ ] MySQL app user password generated (32+ chars)
  - [ ] JWT secret generated: `openssl rand -base64 32`
  - [ ] All stored securely (password manager)

- [ ] **SSL Certificate**
  - [ ] Plan for Let's Encrypt (free) or purchase certificate
  - [ ] Email address for certificate: `_______________`
  - [ ] Domain validation method selected

- [ ] **Firewall Rules**
  - [ ] HTTP (80) allowed from anywhere
  - [ ] HTTPS (443) allowed from anywhere
  - [ ] SSH (22) limited to known IPs (optional but recommended)
  - [ ] MySQL (3306) blocked from public (only internal)

---

## 📝 Documentation & Backup

- [ ] **Documentation Updated**
  - [ ] API endpoints documented
  - [ ] Database schema documented
  - [ ] Deployment steps written down
  - [ ] Rollback procedure documented

- [ ] **Backups Prepared**
  - [ ] Local backup of entire project
  - [ ] Database schema exported
  - [ ] Current data exported (if applicable)
  - [ ] Upload directory backed up

- [ ] **Deployment Scripts**
  - [ ] `quick-start-ovh.sh` reviewed
  - [ ] `deploy-to-ovh.sh` reviewed
  - [ ] `health-check.sh` reviewed
  - [ ] Scripts copied to project root

---

## 🚀 Deployment Day Preparation

### Last Hour Before Deployment
- [ ] Close all uncommitted changes
- [ ] Latest code committed to git
- [ ] All environment variables in `.env` file
- [ ] Backup current database (local)
- [ ] Backup current files (local)
- [ ] Test domain DNS resolution
- [ ] Verify server SSH access works

### Deployment Ready Check
- [ ] Server has minimum specs: 2vCPU, 4GB RAM, 40GB SSD
- [ ] Server SSH access confirmed
- [ ] Domain DNS pointing to server
- [ ] All scripts made executable: `chmod +x *.sh`
- [ ] `.env.production` ready with values
- [ ] Internet connection stable
- [ ] Estimated downtime: 5-10 minutes

---

## 📋 Communication Plan

- [ ] **Notify Stakeholders** (if applicable)
  - [ ] Email sent about deployment time
  - [ ] Expected duration: 30-45 minutes
  - [ ] Expected downtime: 5-10 minutes

- [ ] **Monitoring Setup**
  - [ ] Know how to check logs: `docker-compose logs -f`
  - [ ] Know how to check health: `curl http://domain/api/actuator/health`
  - [ ] Support contact information ready

---

## ✅ Final Pre-Deployment Verification

**Run these commands to verify everything:**

### Local Machine
```bash
# Verify Docker Compose syntax
docker-compose config

# Verify images build (if not using registry)
docker-compose build --no-cache

# Test database migrations locally
# (if you have Docker running locally)
docker-compose up -d
docker-compose logs -f
```

### On OVH Server
```bash
# Verify SSH access
ssh root@your-server-ip "echo 'SSH Access OK'"

# Verify Docker installation
ssh root@your-server-ip "docker --version"
ssh root@your-server-ip "docker-compose --version"

# Verify disk space
ssh root@your-server-ip "df -h"
```

---

## 🎯 Deployment Sequence

### 1. **Initial Setup** (5 minutes)
- [ ] Connect to server via SSH
- [ ] Run `sudo bash quick-start-ovh.sh`

### 2. **Configuration** (5 minutes)
- [ ] Update `.env` with production values
- [ ] Verify configuration: `cat .env`

### 3. **Building & Deployment** (20 minutes)
- [ ] Run `sudo bash deploy-to-ovh.sh`
- [ ] Monitor logs: `docker-compose logs -f`

### 4. **Verification** (5 minutes)
- [ ] Run health checks
- [ ] Test all endpoints
- [ ] Test frontend in browser

### 5. **SSL Certificate** (5 minutes)
- [ ] Install Let's Encrypt certificate
- [ ] Verify HTTPS works
- [ ] Test redirect HTTP → HTTPS

### 6. **Final Testing** (10 minutes)
- [ ] Test all pages load
- [ ] Test API endpoints
- [ ] Test file uploads
- [ ] Test contact form

---

## 🆘 If Something Goes Wrong

**Don't Panic!** You have backups.

1. **Check logs first**
   ```bash
   docker-compose logs -f
   ```

2. **Common issues** (see OVH-DEPLOYMENT-GUIDE.md):
   - Port already in use
   - Database connection failed
   - Disk space full
   - Out of memory

3. **Rollback if needed**
   ```bash
   bash rollback.sh backup_20260803_123456
   ```

4. **Contact support**
   - OVH Manager → Support
   - Include error logs
   - Include screenshots

---

## 📊 Post-Deployment Checklist

After deployment succeeds:

- [ ] Frontend loads: `https://yourdomain.com`
- [ ] Backend health: `https://yourdomain.com/api/actuator/health`
- [ ] All pages accessible
- [ ] Forms submit successfully
- [ ] Images display correctly
- [ ] API calls work
- [ ] SSL certificate valid
- [ ] Database accessible
- [ ] Backups created
- [ ] Monitoring set up

---

## 🎉 Congratulations!

Your MSSD website is now **LIVE** on OVH Cloud!

### Next Steps:
1. Monitor for 24 hours
2. Check logs daily
3. Set up automated backups
4. Plan security updates
5. Configure monitoring alerts

---

## 📞 Important Contacts

- **OVH Support**: Create ticket from manager
- **Your Domain**: Registrar contact info
- **SSL Provider**: Let's Encrypt support
- **Your Team**: Emergency contact

---

**Status**: Ready for Deployment ✅

**Last Updated**: August 2026

**Need Help?** See OVH-DEPLOYMENT-GUIDE.md for detailed instructions.
