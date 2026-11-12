#!/bin/sh

# ============================================
# Nginx SSL Configuration Entrypoint
# ============================================

set -e

APP_DOMAIN="${APP_DOMAIN:-localhost}"
BACKEND_HOST="${BACKEND_HOST:-mssd-backend}"
BACKEND_PORT="${BACKEND_PORT:-8080}"

echo "Configuring Nginx with:"
echo "  APP_DOMAIN: $APP_DOMAIN"
echo "  BACKEND_HOST: $BACKEND_HOST"
echo "  BACKEND_PORT: $BACKEND_PORT"

# Replace variables in nginx config
envsubst < /etc/nginx/conf.d/default.template > /etc/nginx/conf.d/default.conf

# Validate configuration
nginx -t

# Start nginx
exec nginx -g "daemon off;"
