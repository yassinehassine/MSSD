# IntelliJ Database Troubleshooting Guide

## Issue: Database not created when running in IntelliJ

### Quick Fix Steps:

1. **Clean and Rebuild Project**
   - Go to `Build` → `Clean Project`
   - Then `Build` → `Rebuild Project`

2. **Invalidate Caches and Restart**
   - Go to `File` → `Invalidate Caches and Restart`
   - Choose `Invalidate and Restart`

3. **Check Run Configuration**
   - Right-click on `MssdApplication.java`
   - Select `Run 'MssdApplication'`
   - Make sure the working directory is set to the project root

4. **Verify Application Properties**
   - Ensure `src/main/resources/application.properties` is in the classpath
   - Check that the file contains the correct database configuration

### Database Configuration (Fixed)

The application now uses:
```properties
# Database Configuration (H2 in-memory)
spring.datasource.url=jdbc:h2:mem:mssddd;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
```

### Testing the Database

1. **Start the Application**
   - Run `MssdApplication.java` in IntelliJ
   - Check the console for any errors

2. **Test Database Connection**
   - Open browser and go to: `http://localhost:8080/database-test.html`
   - This will show if the database is working

3. **Check H2 Console**
   - Open browser and go to: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:mssddd`
   - Username: `sa`
   - Password: `password`

4. **Test APIs**
   - Categories: `http://localhost:8080/api/categories`
   - Formations: `http://localhost:8080/api/formations`
   - Database Test: `http://localhost:8080/api/test/database`

### Common Issues and Solutions

#### Issue 1: "Table not found" errors
**Solution**: The database is not being created properly
- Check that `spring.jpa.hibernate.ddl-auto=create-drop` is set
- Ensure no other applications are using the same database URL

#### Issue 2: "Connection refused" errors
**Solution**: Port 8080 might be in use
- Change the port in `application.properties`: `server.port=8081`
- Or stop other applications using port 8080

#### Issue 3: "Bean creation failed" errors
**Solution**: Missing dependencies or configuration issues
- Check that all required dependencies are in `pom.xml`
- Verify that all Java files are in the correct packages

#### Issue 4: Seed data not loading
**Solution**: Check the console logs for seeding messages
- Look for "Starting database seeding..." messages
- If not found, check `SeedDataConfig.java` for errors

### Debug Steps

1. **Enable Debug Logging**
   The application now has enhanced logging:
   ```properties
   logging.level.com.mssd=DEBUG
   logging.level.org.hibernate.SQL=DEBUG
   logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
   ```

2. **Check Console Output**
   Look for these messages:
   - "Starting database seeding..."
   - "Categories seeded successfully"
   - "Formations seeded successfully"
   - "Company data seeded successfully"
   - "Highlights seeded successfully"

3. **Test Individual Components**
   - Test database connection: `http://localhost:8080/api/test/database`
   - Check H2 console: `http://localhost:8080/h2-console`
   - Test individual APIs

### Expected Database Tables

After successful startup, you should see these tables in H2 console:
- `calendars`
- `categories`
- `company`
- `contacts`
- `custom_requests`
- `formations`
- `highlights`
- `newsletters`
- `reservations`
- `users`

### Expected Seed Data

- **Categories**: 3 records (Management, Sales, Soft Skills)
- **Formations**: 2 records (Leadership Essentials, Advanced Sales Techniques)
- **Company**: 1 record (company information)
- **Highlights**: 2 records (welcome messages)

### If Still Not Working

1. **Check IntelliJ Logs**
   - Go to `Help` → `Show Log in Explorer`
   - Look for any error messages

2. **Run from Command Line**
   - Open terminal in project directory
   - Run: `mvn spring-boot:run`

3. **Check Java Version**
   - Ensure you're using Java 17 or higher
   - Check: `java -version`

4. **Verify Project Structure**
   - Ensure all files are in the correct packages
   - Check that `src/main/resources` is marked as resources folder

### Contact Support

If none of the above solutions work, please provide:
1. IntelliJ version
2. Java version
3. Console error messages
4. Screenshot of the project structure 