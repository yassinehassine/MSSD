#!/bin/bash

# ============================================
# MSSD OVH Quick Start Script
# ============================================
# Run this script ONCE on a fresh OVH server
# It will install all prerequisites and deploy
# ============================================

set -e

echo "🚀 MSSD OVH Quick Start"
echo "================================"
echo "This will install and configure everything needed for MSSD"
echo ""
echo "⏱️  Estimated time: 10-15 minutes"
echo ""

# Check if running as root
if [ "$EUID" -ne 0 ]; then 
    echo "Please run as root: sudo bash quick-start-ovh.sh"
    exit 1
fi

read -p "Server IP or Domain: " SERVER_DOMAIN
read -p "MySQL Root Password: " -s MYSQL_ROOT_PASS
echo ""
read -p "MySQL Database Name [mssd_production]: " MYSQL_DB
MYSQL_DB=${MYSQL_DB:-mssd_production}
read -p "MySQL User [mssd_admin]: " MYSQL_USER
MYSQL_USER=${MYSQL_USER:-mssd_admin}
read -p "MySQL Password: " -s MYSQL_PASS
echo ""

echo ""
echo "Configuration:"
echo "  Domain: $SERVER_DOMAIN"
echo "  Database: $MYSQL_DB"
echo "  DB User: $MYSQL_USER"
echo ""
read -p "Proceed with installation? (yes/no): " CONFIRM

if [ "$CONFIRM" != "yes" ]; then
    echo "Installation cancelled"
    exit 0
fi

# Step 1: System Update
echo ""
echo "📦 Step 1/7: Updating system..."
apt update && apt upgrade -y

# Step 2: Install Docker
echo ""
echo "🐳 Step 2/7: Installing Docker..."
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh
    rm get-docker.sh
    systemctl start docker
    systemctl enable docker
    echo "✅ Docker installed"
else
    echo "✅ Docker already installed"
fi

# Step 3: Install Docker Compose
echo ""
echo "🐳 Step 3/7: Installing Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    apt install docker-compose -y
    echo "✅ Docker Compose installed"
else
    echo "✅ Docker Compose already installed"
fi

# Step 4: Install Git
echo ""
echo "📥 Step 4/7: Installing Git..."
if ! command -v git &> /dev/null; then
    apt install git -y
    echo "✅ Git installed"
else
    echo "✅ Git already installed"
fi

# Step 5: Clone/Setup Project
echo ""
echo "📁 Step 5/7: Setting up project..."
mkdir -p /opt
cd /opt

if [ ! -d "MSSD-mssd" ]; then
    read -p "Git repository URL (or press Enter to skip): " GIT_REPO
    if [ ! -z "$GIT_REPO" ]; then
        git clone "$GIT_REPO" MSSD-mssd
    else
        echo "⚠️  Please upload your project to /opt/MSSD-mssd manually"
        exit 1
    fi
fi

cd MSSD-mssd

# Step 6: Configure Environment
echo ""
echo "⚙️  Step 6/7: Configuring environment..."

cat > .env << EOF
# MSSD Production Environment
MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASS
MYSQL_DATABASE=$MYSQL_DB
MYSQL_USER=$MYSQL_USER
MYSQL_PASSWORD=$MYSQL_PASS
SPRING_PROFILES_ACTIVE=prod
APP_UPLOAD_DIR=/app/uploads
FRONTEND_PORT=80
EOF

echo "✅ Environment configured"

# Step 7: Deploy Application
echo ""
echo "🚀 Step 7/7: Deploying application..."

docker-compose up -d --build

echo ""
echo "⏳ Waiting for services to start (this may take 2-3 minutes)..."
sleep 60

# Check status
echo ""
echo "================================"
echo "📊 Deployment Status"
echo "================================"
docker-compose ps

echo ""
echo "================================"
echo "✅ Installation Complete!"
echo "================================"
echo ""
echo "🌐 Your application is deploying at:"
echo "   http://$SERVER_DOMAIN"
echo ""
echo "🔍 Check health:"
echo "   curl http://$SERVER_DOMAIN/api/actuator/health"
echo ""
echo "📝 View logs:"
echo "   cd /opt/MSSD-mssd"
echo "   docker-compose logs -f"
echo ""
echo "🔒 Next steps:"
echo "   1. Point your domain DNS to this server IP"
echo "   2. Install SSL certificate:"
echo "      apt install certbot python3-certbot-nginx -y"
echo "      certbot --nginx -d $SERVER_DOMAIN"
echo ""
echo "📖 Full documentation: cat OVH-DEPLOYMENT-GUIDE.md"
echo ""
