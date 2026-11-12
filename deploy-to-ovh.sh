#!/bin/bash

# ============================================
# MSSD OVH Deployment Script
# ============================================
# This script automates the deployment process
# Run on your OVH server after initial setup
# ============================================

set -e  # Exit on error

echo "🚀 MSSD OVH Deployment Script"
echo "================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
PROJECT_DIR="/opt/MSSD-mssd"
BACKUP_DIR="/opt/mssd-backups"
LOG_FILE="/var/log/mssd-deploy.log"

# Functions
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1" | tee -a "$LOG_FILE"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1" | tee -a "$LOG_FILE"
    exit 1
}

warn() {
    echo -e "${YELLOW}[WARNING]${NC} $1" | tee -a "$LOG_FILE"
}

# Check if running as root
if [ "$EUID" -ne 0 ]; then 
    error "Please run as root (sudo)"
fi

# Step 1: Pre-deployment checks
log "Step 1: Running pre-deployment checks..."

if [ ! -d "$PROJECT_DIR" ]; then
    error "Project directory not found: $PROJECT_DIR"
fi

if ! command -v docker &> /dev/null; then
    error "Docker is not installed"
fi

if ! command -v docker-compose &> /dev/null; then
    error "Docker Compose is not installed"
fi

log "✅ Pre-deployment checks passed"

# Step 2: Backup current deployment
log "Step 2: Creating backup..."

mkdir -p "$BACKUP_DIR"
BACKUP_NAME="backup_$(date +%Y%m%d_%H%M%S)"

cd "$PROJECT_DIR"

# Backup database
if docker ps | grep -q mssd-db; then
    log "Backing up database..."
    docker exec mssd-db mysqldump -u root -p${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} > "$BACKUP_DIR/${BACKUP_NAME}_db.sql" || warn "Database backup failed"
fi

# Backup uploaded files
if [ -d "$PROJECT_DIR/uploads" ]; then
    log "Backing up uploads..."
    tar -czf "$BACKUP_DIR/${BACKUP_NAME}_uploads.tar.gz" uploads/ || warn "Upload backup failed"
fi

log "✅ Backup completed: $BACKUP_NAME"

# Step 3: Pull latest code
log "Step 3: Pulling latest code..."

cd "$PROJECT_DIR"

if [ -d ".git" ]; then
    git fetch origin
    git pull origin main || warn "Git pull failed - continuing with existing code"
    log "✅ Code updated from repository"
else
    warn "Not a git repository - skipping code update"
fi

# Step 4: Stop running containers
log "Step 4: Stopping running containers..."

docker-compose down || warn "Failed to stop containers"
log "✅ Containers stopped"

# Step 5: Build new images
log "Step 5: Building Docker images..."

docker-compose build --no-cache || error "Docker build failed"
log "✅ Docker images built"

# Step 6: Start services
log "Step 6: Starting services..."

docker-compose up -d || error "Failed to start services"
log "✅ Services started"

# Step 7: Wait for services to be healthy
log "Step 7: Waiting for services to be healthy..."

# Wait for database
log "Waiting for database..."
for i in {1..30}; do
    if docker exec mssd-db mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD} --silent 2>/dev/null; then
        log "✅ Database is ready"
        break
    fi
    if [ $i -eq 30 ]; then
        error "Database failed to start"
    fi
    sleep 2
done

# Wait for backend
log "Waiting for backend..."
for i in {1..60}; do
    if curl -s http://localhost:8082/actuator/health | grep -q "UP"; then
        log "✅ Backend is ready"
        break
    fi
    if [ $i -eq 60 ]; then
        error "Backend failed to start"
    fi
    sleep 2
done

# Wait for frontend
log "Waiting for frontend..."
for i in {1..30}; do
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:80 | grep -q "200\|301\|302"; then
        log "✅ Frontend is ready"
        break
    fi
    if [ $i -eq 30 ]; then
        error "Frontend failed to start"
    fi
    sleep 2
done

# Step 8: Clean up old images
log "Step 8: Cleaning up old Docker images..."
docker image prune -f || warn "Failed to prune images"
log "✅ Cleanup completed"

# Step 9: Display status
log "Step 9: Deployment summary..."

echo ""
echo "================================"
echo "📊 Deployment Status"
echo "================================"
docker-compose ps
echo ""

# Test endpoints
echo "🔍 Health Checks:"
echo "--------------------------------"

# Backend health
BACKEND_HEALTH=$(curl -s http://localhost:8082/actuator/health | jq -r '.status' 2>/dev/null || echo "UNKNOWN")
echo "Backend API: $BACKEND_HEALTH"

# Frontend check
FRONTEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:80)
echo "Frontend: HTTP $FRONTEND_STATUS"

# Database check
if docker exec mssd-db mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD} --silent 2>/dev/null; then
    echo "Database: UP"
else
    echo "Database: DOWN"
fi

echo "================================"
echo ""

# Display logs path
log "📝 Logs are available at: $LOG_FILE"
log "📝 View real-time logs: docker-compose logs -f"
log "📝 Backup location: $BACKUP_DIR/$BACKUP_NAME"

echo ""
echo "================================"
echo "✅ Deployment Completed Successfully!"
echo "================================"
echo ""
echo "🌐 Your application should be available at:"
echo "   - Frontend: http://your-domain.com"
echo "   - Backend API: http://your-domain.com/api"
echo "   - Health Check: http://your-domain.com/api/actuator/health"
echo ""
echo "📊 Monitor your application:"
echo "   docker-compose logs -f"
echo ""
echo "🔄 Rollback if needed:"
echo "   bash rollback.sh $BACKUP_NAME"
echo ""

exit 0
