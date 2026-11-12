#!/bin/bash

# ============================================
# Docker Database Backup Script
# ============================================

set -e

# Configuration
BACKUP_DIR="./backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/mysql_backup_$TIMESTAMP.sql"
CONTAINER_NAME="mssd-db"
MYSQL_USER="${MYSQL_USER:-mssd_user}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-mssd_pass}"
MYSQL_DATABASE="${MYSQL_DATABASE:-mssd_db}"
RETENTION_DAYS=30

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

print_header() {
  echo -e "${BLUE}=====================================${NC}"
  echo -e "${BLUE}$1${NC}"
  echo -e "${BLUE}=====================================${NC}\n"
}

print_success() {
  echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
  echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
  echo -e "${RED}✗ $1${NC}"
}

print_header "MySQL Database Backup"

# Create backup directory
if [ ! -d "$BACKUP_DIR" ]; then
  mkdir -p "$BACKUP_DIR"
  print_success "Created backup directory: $BACKUP_DIR"
fi

# Check if container is running
if ! docker ps | grep -q "$CONTAINER_NAME"; then
  print_error "Container $CONTAINER_NAME is not running"
  exit 1
fi

print_success "Container $CONTAINER_NAME is running"

# Create backup
echo -e "${BLUE}Creating backup: $BACKUP_FILE${NC}"
docker exec "$CONTAINER_NAME" mysqldump \
  -u "$MYSQL_USER" \
  -p"$MYSQL_PASSWORD" \
  "$MYSQL_DATABASE" > "$BACKUP_FILE"

# Compress backup
gzip "$BACKUP_FILE"
BACKUP_FILE="$BACKUP_FILE.gz"

if [ -f "$BACKUP_FILE" ]; then
  SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
  print_success "Backup created successfully"
  print_success "File: $BACKUP_FILE"
  print_success "Size: $SIZE"
else
  print_error "Backup failed"
  exit 1
fi

# Cleanup old backups
echo -e "\n${BLUE}Cleaning up old backups (older than $RETENTION_DAYS days)...${NC}"
find "$BACKUP_DIR" -name "mysql_backup_*.sql.gz" -mtime +$RETENTION_DAYS -delete
print_success "Old backups cleaned up"

# List recent backups
echo -e "\n${BLUE}Recent backups:${NC}"
ls -lh "$BACKUP_DIR"/mysql_backup_*.sql.gz 2>/dev/null | tail -5

print_header "Backup Complete"
echo -e "${GREEN}Database backup completed successfully!${NC}"
