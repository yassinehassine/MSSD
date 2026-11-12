@echo off
REM ============================================================================
REM MSSD Database Seeder - Quick Start Script (Windows)
REM ============================================================================
REM This script runs the Spring Boot application with the 'seed' profile
REM to automatically populate the database with sample data.
REM ============================================================================

echo.
echo ========================================
echo   MSSD Database Seeder
echo ========================================
echo.
echo This will:
echo   1. Start the Spring Boot application
echo   2. Clear existing data (WARNING!)
echo   3. Populate database with sample data
echo.

set /p confirm="Are you sure you want to proceed? (Y/N): "
if /i not "%confirm%"=="Y" (
    echo Operation cancelled.
    pause
    exit /b
)

echo.
echo Starting database seeding...
echo.

REM Run Spring Boot with seed profile
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=seed

echo.
echo ========================================
echo   Seeding Complete!
echo ========================================
echo.
echo The database has been populated with:
echo   - 3 users (admin, manager, instructor)
echo   - 6 themes
echo   - 18 formations
echo   - 8 reviews
echo   - 8 portfolio items
echo   - 8 calendar events
echo   - 10 blog posts
echo   - 5 newsletter subscriptions
echo.
echo Login credentials:
echo   Username: admin
echo   Password: admin123
echo.
echo IMPORTANT: Remove the 'seed' profile before
echo            deploying to production!
echo.
pause
