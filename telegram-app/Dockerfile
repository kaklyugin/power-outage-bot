# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven/Gradle project files
COPY pom.xml .
COPY src ./src

# Build the application with the 'dev' profile
# For Maven:
RUN mvn clean package -DskipTests -Dspring.profiles.active=${APP_ENV}

# For Gradle (if using Gradle, uncomment and adjust):
# COPY build.gradle settings.gradle ./
# RUN gradle clean build -x test -Pdev

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your application listens on (e.g., 8080)
EXPOSE 8080

# Run the application with the 'dev' profile activated
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=${APP_ENV}"]