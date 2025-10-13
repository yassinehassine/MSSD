@echo off
echo ========================================
echo MSSD Backend Testing Script
echo ========================================

echo.
echo 1. Checking Java version...
java --version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo 2. Checking Maven version...
mvn --version
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo.
echo 3. Cleaning and building project...
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo 4. Running tests...
mvn test
if %errorlevel% neq 0 (
    echo ERROR: Tests failed
    pause
    exit /b 1
)

echo.
echo 5. Starting the application...
echo The application will start on http://localhost:8080
echo Press Ctrl+C to stop the application
echo.
mvn spring-boot:run

pause 