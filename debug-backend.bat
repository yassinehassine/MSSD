@echo off
echo ========================================
echo MSSD Backend Debug Script
echo ========================================

echo 1. Checking if MySQL is running...
netstat -an | findstr :3306
if %ERRORLEVEL% neq 0 (
    echo ERROR: MySQL is not running on port 3306
    echo Please start MySQL service
    pause
    exit /b 1
)

echo 2. Testing MySQL connection...
mysql -u root -p -e "SHOW DATABASES;" 2>nul
if %ERRORLEVEL% neq 0 (
    echo ERROR: Cannot connect to MySQL
    echo Please check MySQL credentials
    pause
    exit /b 1
)

echo 3. Checking if MSSDD database exists...
mysql -u root -p -e "USE MSSDD; SHOW TABLES;" 2>nul
if %ERRORLEVEL% neq 0 (
    echo Creating MSSDD database...
    mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS MSSDD;"
)

echo 4. Listing Java processes...
jps -l

echo ========================================
echo Backend environment ready!
echo You can now try to start the backend
echo ========================================
pause
