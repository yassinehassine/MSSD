# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create upload directory
RUN mkdir -p /app/uploads

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check (uses PORT env var so it works on Render where PORT differs from 8080)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -q --spider http://localhost:${PORT:-8080}/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", \
  "--spring.profiles.active=prod", \
  "--app.upload.dir=/app/uploads"]
