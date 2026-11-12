#!/bin/bash

# ============================================
# Generate SSL/TLS Certificates
# ============================================

set -e

CERTS_DIR="./certs"
DOMAIN="${1:-yourdomain.com}"
DAYS="${2:-365}"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}SSL Certificate Generation${NC}"
echo -e "${BLUE}=====================================${NC}\n"

# Create certs directory
if [ ! -d "$CERTS_DIR" ]; then
  mkdir -p "$CERTS_DIR"
  echo -e "${GREEN}✓ Created certs directory${NC}"
fi

# Check for existing certificates
if [ -f "$CERTS_DIR/cert.pem" ] && [ -f "$CERTS_DIR/key.pem" ]; then
  echo -e "${YELLOW}⚠ Certificates already exist in $CERTS_DIR${NC}"
  read -p "Overwrite existing certificates? (y/n) " -n 1 -r
  echo
  if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Skipping certificate generation"
    exit 0
  fi
fi

echo -e "${BLUE}Generating self-signed certificate for: $DOMAIN${NC}"
echo -e "${BLUE}Valid for: $DAYS days${NC}\n"

# Generate private key and certificate
openssl req -x509 -newkey rsa:4096 -keyout "$CERTS_DIR/key.pem" -out "$CERTS_DIR/cert.pem" -days "$DAYS" -nodes \
  -subj "/C=US/ST=State/L=City/O=Organization/CN=$DOMAIN"

# Set correct permissions
chmod 600 "$CERTS_DIR/key.pem"
chmod 644 "$CERTS_DIR/cert.pem"

echo -e "${GREEN}✓ Certificate generated successfully${NC}"
echo -e "${GREEN}✓ Private key: $CERTS_DIR/key.pem${NC}"
echo -e "${GREEN}✓ Certificate: $CERTS_DIR/cert.pem${NC}\n"

echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}Certificate Information:${NC}"
echo -e "${BLUE}=====================================${NC}"
openssl x509 -in "$CERTS_DIR/cert.pem" -text -noout | grep -E "Subject:|Issuer:|Not Before|Not After"

echo -e "\n${YELLOW}Note: This is a self-signed certificate for development/testing.${NC}"
echo -e "${YELLOW}For production, use Let's Encrypt or a trusted CA.${NC}"
