# MSSD DevOps Guide

Complete Docker and CI/CD setup for the MSSD project (Spring Boot + Angular).

## 🚀 Quick Start

### Prerequisites
- Docker Desktop installed
- Docker Compose v2.x
- Git

### Launch the Application

```bash
# Build and start all services
docker compose up -d

# View logs
docker compose logs -f

# Check status
docker compose ps
```

**Access URLs:**
- Frontend: http://localhost
- Backend API: http://localhost:8080
- phpMyAdmin (debug mode): http://localhost:8081
- MySQL: localhost:3307

## 📁 File Structure

```
MSSD-mssd/
├── docker-compose.yml          # Multi-container orchestration
├── .env                        # Environment variables
├── Makefile                    # Quick commands
├── .github/
│   └── workflows/
│       └── ci-cd.yml          # GitHub Actions pipeline
├── mssd-backend/
│   ├── Dockerfile             # Backend image
│   ├── .dockerignore
│   └── src/main/resources/
│       └── application-prod.properties
└── mssd-frontend/
    ├── Dockerfile             # Frontend image  
    ├── nginx.conf             # Nginx config
    ├── .dockerignore
    └── src/environments/
        ├── environment.ts     # Dev config
        └── environment.prod.ts # Prod config
```

## 🐳 Docker Commands

### Basic Operations
```bash
# Start services
docker compose up -d

# Start with phpMyAdmin
docker compose --profile debug up -d

# Stop services
docker compose down

# Rebuild after code changes
docker compose up -d --build

# View logs
docker compose logs -f
docker compose logs -f mssd-backend
docker compose logs -f mssd-frontend
```

### Shell Access
```bash
# MySQL shell
docker compose exec mssd-db mysql -u root -proot123 mssd_db

# Backend container
docker compose exec mssd-backend sh

# Frontend container
docker compose exec mssd-frontend sh
```

### Cleanup
```bash
# Remove containers and volumes
docker compose down -v

# Full cleanup (including images)
docker compose down -v --rmi all
docker system prune -f
```

## 🔧 Environment Variables

Edit `.env` file:

```env
# Database
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=mssd_db
MYSQL_USER=mssd_user
MYSQL_PASSWORD=mssd_pass

# Backend
SPRING_PROFILES_ACTIVE=prod
APP_UPLOAD_DIR=/app/uploads
```

## 🔄 CI/CD Pipeline (GitHub Actions)

### Workflow Stages
1. **Backend Build & Test** - Maven compile and unit tests
2. **Frontend Build & Lint** - npm build and linting
3. **Docker Build & Push** - Build and push images to Docker Hub
4. **Deploy** - SSH deploy to production server

### Required GitHub Secrets

Go to repo **Settings → Secrets** and add:

| Secret | Description |
|--------|-------------|
| `DOCKER_USERNAME` | Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub access token |
| `SERVER_HOST` | Production server IP |
| `SERVER_USER` | SSH username |
| `SERVER_SSH_KEY` | SSH private key |

### Trigger Pipeline
```bash
# Push to main branch triggers full CI/CD
git push origin main

# Pull requests trigger build/test only
git checkout -b feature/my-feature
git push origin feature/my-feature
```

## 🏗️ Architecture

### Multi-Stage Docker Builds
- **Backend**: Maven build → JRE-only runtime (reduces image size)
- **Frontend**: Node.js build → Nginx static server

### Services
- **mssd-db**: MySQL 8.0 with persistent volumes
- **mssd-backend**: Spring Boot on port 8080
- **mssd-frontend**: Nginx serving Angular on port 80
- **phpmyadmin**: Database UI (debug profile only)

### Networking
- All services on `mssd-network` bridge
- Frontend proxies `/api/*` to backend
- Backend connects to database via service name

### Health Checks
- Backend: `/actuator/health` endpoint
- Frontend: HTTP status check
- Database: `mysqladmin ping`

## 📊 Monitoring

### Check Service Health
```bash
# All services status
docker compose ps

# Backend health
curl http://localhost:8080/actuator/health

# Frontend health
curl -I http://localhost

# Database health
docker compose exec mssd-db mysqladmin ping -h localhost -u root -proot123
```

### View Resource Usage
```bash
docker stats
```

## 🔒 Security Best Practices

1. **Change default passwords** in `.env` for production
2. **Use secrets management** for sensitive data
3. **Enable HTTPS** with reverse proxy (Nginx/Traefik)
4. **Restrict exposed ports** in production
5. **Regular security updates**: `docker compose pull`

## 🚨 Troubleshooting

### Container Won't Start
```bash
# Check logs
docker compose logs -f SERVICE_NAME

# Restart specific service
docker compose restart SERVICE_NAME
```

### Database Connection Issues
```bash
# Verify database is ready
docker compose exec mssd-db mysqladmin ping

# Check network
docker network inspect mssd-mssd_mssd-network
```

### Frontend Can't Reach Backend
```bash
# Check backend health
curl http://localhost:8080/actuator/health

# Verify nginx proxy config
docker compose exec mssd-frontend cat /etc/nginx/conf.d/default.conf
```

### Port Already in Use
```bash
# Find process using port
netstat -ano | findstr :80
netstat -ano | findstr :8080

# Kill process or change port in docker-compose.yml
```

## 📈 Production Deployment

### Server Setup
```bash
# On production server
git clone YOUR_REPO
cd MSSD-mssd

# Edit production .env
nano .env

# Start services
docker compose up -d

# Enable auto-restart
docker update --restart=unless-stopped $(docker ps -aq)
```

### SSL/HTTPS Setup (with Caddy)
```yaml
# Add to docker-compose.yml
caddy:
  image: caddy:2-alpine
  ports:
    - "443:443"
  volumes:
    - ./Caddyfile:/etc/caddy/Caddyfile
    - caddy_data:/data
  networks:
    - mssd-network
```

## 📝 Makefile Commands

```bash
make help           # Show all commands
make build          # Build images
make up             # Start services
make down           # Stop services
make logs           # View logs
make status         # Service status
make db-shell       # MySQL shell
make clean          # Full cleanup
```

---

**Need Help?** Check logs first: `docker compose logs -f`
