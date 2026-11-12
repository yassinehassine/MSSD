#!/bin/bash

# ============================================
# MSSD Health Check Script
# ============================================
# Check all services are running properly
# ============================================

echo "🏥 MSSD Health Check"
echo "================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configuration
PROJECT_DIR="/opt/MSSD-mssd"

check_service() {
    local service_name=$1
    local check_command=$2
    
    echo -n "Checking $service_name... "
    if eval "$check_command" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ UP${NC}"
        return 0
    else
        echo -e "${RED}❌ DOWN${NC}"
        return 1
    fi
}

cd "$PROJECT_DIR" 2>/dev/null || cd /opt/MSSD-mssd 2>/dev/null || true

echo "🐳 Docker Containers:"
echo "--------------------------------"
docker-compose ps 2>/dev/null || docker ps

echo ""
echo "🔍 Service Health:"
echo "--------------------------------"

# Check Docker
check_service "Docker" "docker ps"

# Check MySQL
check_service "MySQL Database" "docker exec mssd-db mysqladmin ping -h localhost --silent"

# Check Backend
BACKEND_HEALTH=$(curl -s http://localhost:8082/actuator/health 2>/dev/null | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
echo -n "Backend API... "
if [ "$BACKEND_HEALTH" = "UP" ]; then
    echo -e "${GREEN}✅ UP${NC}"
else
    echo -e "${RED}❌ DOWN (Status: $BACKEND_HEALTH)${NC}"
fi

# Check Frontend
FRONTEND_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:80 2>/dev/null)
echo -n "Frontend... "
if [ "$FRONTEND_CODE" = "200" ] || [ "$FRONTEND_CODE" = "301" ]; then
    echo -e "${GREEN}✅ UP (HTTP $FRONTEND_CODE)${NC}"
else
    echo -e "${RED}❌ DOWN (HTTP $FRONTEND_CODE)${NC}"
fi

echo ""
echo "💾 System Resources:"
echo "--------------------------------"
echo "Disk Usage:"
df -h / | tail -1 | awk '{print "  Used: "$3" / "$2" ("$5")"}'

echo "Memory Usage:"
free -h | grep Mem | awk '{print "  Used: "$3" / "$2}'

echo "CPU Load:"
uptime | awk -F'load average:' '{print "  "$2}'

echo ""
echo "📊 Docker Stats (5 seconds snapshot):"
echo "--------------------------------"
timeout 5 docker stats --no-stream 2>/dev/null || echo "Docker stats unavailable"

echo ""
echo "📝 Recent Errors (last 10):"
echo "--------------------------------"
docker-compose logs --tail=10 2>/dev/null | grep -i error || echo "No recent errors"

echo ""
echo "================================"
echo "Health check complete"
echo "================================"
