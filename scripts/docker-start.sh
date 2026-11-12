#!/bin/bash

# ============================================
# Docker Compose Start Script
# ============================================

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
COMPOSE_FILE="${1:-docker-compose.yml}"
PROFILE="${2:-}"

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

# Check for Docker
if ! command -v docker &> /dev/null; then
  print_error "Docker is not installed"
  exit 1
fi

if ! command -v docker-compose &> /dev/null; then
  print_error "Docker Compose is not installed"
  exit 1
fi

print_header "MSSD Docker Compose Startup"

# Check if .env file exists
if [ ! -f ".env" ]; then
  print_warning ".env file not found"
  echo "Creating .env from .env.production template..."
  cp .env.production .env
  print_warning "Please update .env with your configuration"
fi

print_success "Docker is available"
print_success "Docker Compose is available"

# Build and start services
echo -e "\n${BLUE}Building services...${NC}"
if [ -n "$PROFILE" ]; then
  docker-compose -f "$COMPOSE_FILE" --profile "$PROFILE" build --no-cache
else
  docker-compose -f "$COMPOSE_FILE" build --no-cache
fi

echo -e "\n${BLUE}Starting services...${NC}"
if [ -n "$PROFILE" ]; then
  docker-compose -f "$COMPOSE_FILE" --profile "$PROFILE" up -d
else
  docker-compose -f "$COMPOSE_FILE" up -d
fi

print_success "Services started"

# Wait for services to be healthy
echo -e "\n${BLUE}Waiting for services to be ready...${NC}"
sleep 10

# Check service health
if [ -n "$PROFILE" ]; then
  RUNNING=$(docker-compose -f "$COMPOSE_FILE" --profile "$PROFILE" ps -q | wc -l)
else
  RUNNING=$(docker-compose -f "$COMPOSE_FILE" ps -q | wc -l)
fi

if [ "$RUNNING" -gt 0 ]; then
  print_success "All services are running"
  echo -e "\n${BLUE}Service Status:${NC}"
  if [ -n "$PROFILE" ]; then
    docker-compose -f "$COMPOSE_FILE" --profile "$PROFILE" ps
  else
    docker-compose -f "$COMPOSE_FILE" ps
  fi
else
  print_error "No services are running"
  exit 1
fi

print_header "Access Information"
echo -e "Frontend:     ${GREEN}http://localhost${NC}"
echo -e "Backend API:  ${GREEN}http://localhost:8082${NC}"
echo -e "Database:     ${GREEN}localhost:3307${NC}"
echo -e "phpMyAdmin:   ${GREEN}http://localhost:8081${NC} (with --profile debug)"
echo -e "\nServices:     ${GREEN}\$(docker-compose -f $COMPOSE_FILE ps)${NC}"

echo -e "\n${BLUE}Useful Commands:${NC}"
echo -e "View logs:        ${GREEN}docker-compose -f $COMPOSE_FILE logs -f${NC}"
echo -e "Stop services:    ${GREEN}docker-compose -f $COMPOSE_FILE down${NC}"
echo -e "Execute command:  ${GREEN}docker-compose -f $COMPOSE_FILE exec SERVICE_NAME COMMAND${NC}"

print_success "Docker environment is ready!"
