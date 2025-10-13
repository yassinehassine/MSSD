@echo off
echo Starting MSSD Backend Application with H2 Database...
echo.
echo Profile: Development (H2)
echo Port: 8080
echo H2 Console: http://localhost:8080/h2-console
echo.

java -jar target/mssd-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

pause