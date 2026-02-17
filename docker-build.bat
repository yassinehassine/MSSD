@echo off
echo ============================================
echo  Building MSSD Docker Images
echo ============================================
echo.

docker compose build --no-cache

echo.
echo Build complete!
echo.
echo To start services: docker compose up -d
echo.
pause
