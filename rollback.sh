#!/bin/bash

# ============================================
# MSSD Rollback Script
# ============================================
# Rollback to a previous backup
# Usage: bash rollback.sh backup_20260803_123456
# ============================================

set -e

echo "🔄 MSSD Rollback Script"
echo "================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configuration
PROJECT_DIR="/opt/MSSD-mssd"
BACKUP_DIR="/opt/mssd-backups"

# Check if backup name provided
if [ -z "$1" ]; then
    echo "Available backups:"
    ls -la "$BACKUP_DIR" | grep backup_
    echo ""
    echo "Usage: bash rollback.sh <backup_name>"
    echo "Example: bash rollback.sh backup_20260803_123456"
    exit 1
fi

BACKUP_NAME=$1

# Check if backup exists
if [ ! -f "$BACKUP_DIR/${BACKUP_NAME}_db.sql" ]; then
    echo -e "${RED}Backup not found: ${BACKUP_NAME}_db.sql${NC}"
    exit 1
fi

echo -e "${YELLOW}⚠️  WARNING: This will restore your database to backup: $BACKUP_NAME${NC}"
echo "Press Ctrl+C to cancel, or Enter to continue..."
read

# Stop containers
echo "Stopping containers..."
cd "$PROJECT_DIR"
docker-compose down

# Restore database
echo "Restoring database..."
docker-compose up -d mssd-db

# Wait for database
sleep 10

docker exec -i mssd-db mysql -u root -p${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} < "$BACKUP_DIR/${BACKUP_NAME}_db.sql"

# Restore uploads if available
if [ -f "$BACKUP_DIR/${BACKUP_NAME}_uploads.tar.gz" ]; then
    echo "Restoring uploads..."
    cd "$PROJECT_DIR"
    tar -xzf "$BACKUP_DIR/${BACKUP_NAME}_uploads.tar.gz"
fi

# Start all services
echo "Starting all services..."
docker-compose up -d

echo ""
echo -e "${GREEN}✅ Rollback completed!${NC}"
echo "Monitor with: docker-compose logs -f"
