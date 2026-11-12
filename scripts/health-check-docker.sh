#!/bin/bash

# ============================================
# Docker Services Health Check
# ============================================

set -e

# Configuration
COMPOSE_FILE="${1:-docker-compose.yml}"
TIMEOUT=30
RETRY_COUNT=3

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Functions
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

# Check service
check_service() {
  local service=$1
  local check_cmd=$2
  local retries=0

  while [ $retries -lt $RETRY_COUNT ]; do
    if eval "$check_cmd" &> /dev/null; then
      print_success "$service is healthy"
      return 0
    fi
    retries=$((retries + 1))
    if [ $retries -lt $RETRY_COUNT ]; then
      print_warning "Retry $retries/$RETRY_COUNT for $service..."
      sleep 5
    fi
  done

  print_error "$service health check failed after $RETRY_COUNT attempts"
  return 1
}

print_header "Docker Services Health Check"

# Check Docker
if ! command -v docker &> /dev/null; then
  print_error "Docker is not installed"
  exit 1
fi

# Check Docker daemon
if ! docker info &> /dev/null; then
  print_error "Docker daemon is not running"
  exit 1
fi

print_success "Docker daemon is running"

# Get running containers
echo -e "${BLUE}Checking services...${NC}\n"

# Database health check
if docker-compose -f "$COMPOSE_FILE" ps mssd-db &> /dev/null; then
  check_service "MySQL Database" \
    "docker-compose -f $COMPOSE_FILE exec -T mssd-db \
     mysqladmin ping -h localhost -u root -p\${MYSQL_ROOT_PASSWORD:-root123} | grep -q 'mysqld is alive'"
else
  print_warning "MySQL Database service not found"
fi

# Backend health check
if docker-compose -f "$COMPOSE_FILE" ps mssd-backend &> /dev/null; then
  check_service "Spring Boot Backend" \
    "curl -f -s http://localhost:8082/actuator/health > /dev/null"
else
  print_warning "Spring Boot Backend service not found"
fi

# Frontend health check
if docker-compose -f "$COMPOSE_FILE" ps mssd-frontend &> /dev/null; then
  check_service "Angular Frontend" \
    "curl -f -s http://localhost/ > /dev/null"
else
  if docker-compose -f "$COMPOSE_FILE" ps mssd-nginx &> /dev/null; then
    check_service "Nginx Reverse Proxy" \
      "curl -f -s http://localhost/health > /dev/null"
  else
    print_warning "Frontend services not found"
  fi
fi

# Docker container status
echo -e "\n${BLUE}Container Status:${NC}"
docker-compose -f "$COMPOSE_FILE" ps

# Summary
echo -e "\n${BLUE}Resource Usage:${NC}"
docker stats --no-stream

print_header "Health Check Complete"
echo -e "${GREEN}All services are healthy!${NC}"
