@echo off
echo ============================================
echo  Cleaning MSSD Docker Environment
echo ============================================
echo.
echo WARNING: This will remove all containers, volumes, and images!
echo.
set /p confirm="Are you sure? (y/n): "

if /i "%confirm%"=="y" (
    echo.
    echo Removing containers and volumes...
    docker compose down -v --rmi all --remove-orphans
    
    echo.
    echo Cleaning Docker system...
    docker system prune -f
    
    echo.
    echo Cleanup complete!
) else (
    echo.
    echo Cleanup cancelled.
)

echo.
pause
