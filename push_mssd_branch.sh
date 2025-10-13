#!/bin/bash
# Script to push the rescued Mssd branch to remote repository

set -e

echo "================================================"
echo "Mssd Branch Rescue - Push Script"
echo "================================================"
echo ""

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo "Error: Not in a git repository"
    exit 1
fi

# Check if Mssd branch exists
if ! git show-ref --verify --quiet refs/heads/Mssd; then
    echo "Error: Mssd branch does not exist locally"
    echo "Creating Mssd branch from main..."
    git fetch origin main:main 2>/dev/null || true
    git checkout -b Mssd main
    echo "✓ Mssd branch created"
else
    echo "✓ Mssd branch found locally"
fi

# Switch to Mssd branch
echo "Switching to Mssd branch..."
git checkout Mssd

# Push to remote
echo "Pushing Mssd branch to remote repository..."
if git push -u origin Mssd; then
    echo ""
    echo "================================================"
    echo "SUCCESS! ✓"
    echo "================================================"
    echo "The Mssd branch has been successfully pushed to GitHub"
    echo "You can view it at: https://github.com/yassinehassine/MSSD/tree/Mssd"
else
    echo ""
    echo "================================================"
    echo "AUTHENTICATION REQUIRED"
    echo "================================================"
    echo "Please ensure you have the necessary permissions to push to this repository."
    echo "You may need to:"
    echo "  1. Configure your git credentials"
    echo "  2. Use SSH instead of HTTPS"
    echo "  3. Generate a personal access token"
    exit 1
fi
