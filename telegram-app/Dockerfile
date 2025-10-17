# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

ARG IMAGE_VERSION=0.1
ARG APP_NAME=bot
ARG SPRING_PROFILES_ACTIVE=dev

# Use the arguments as needed
LABEL version="${IMAGE_VERSION}"
LABEL name="${APP_NAME}"

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}

# Stage 2: Create the final image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]