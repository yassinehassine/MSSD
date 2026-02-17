@echo off
echo ============================================
echo  Stopping MSSD Docker Containers
echo ============================================
echo.

docker compose down

echo.
echo All containers stopped.
echo.
pause
