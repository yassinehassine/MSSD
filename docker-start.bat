@echo off
echo ============================================
echo  Starting MSSD Docker Containers
echo ============================================
echo.

docker compose up -d

echo.
echo ============================================
echo  Services Started!
echo ============================================
echo  Frontend:    http://localhost
echo  Backend:     http://localhost:8080
echo  phpMyAdmin:  http://localhost:8081 (use --profile debug)
echo  MySQL:       localhost:3307
echo ============================================
echo.
echo To view logs: docker compose logs -f
echo To stop:      docker compose down
echo.
pause
