# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

ARG IMAGE_VERSION=0.1
ARG APP_NAME=crawler
ARG SPRING_PROFILES_ACTIVE=dev

LABEL version="${IMAGE_VERSION}"
LABEL name="${APP_NAME}"

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    && rm -rf /root/.m2

# Stage 2: Create custom JRE
FROM eclipse-temurin:21-jdk-alpine AS jre-builder

WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Extract required modules and create custom JRE
RUN $JAVA_HOME/bin/jdeps --ignore-missing-deps \
    --multi-release 21 \
    --print-module-deps \
    --class-path 'app.jar' app.jar > deps.info

RUN $JAVA_HOME/bin/jlink \
    --add-modules $(cat deps.info),jdk.unsupported \
    --strip-debug \
    --compress=2 \
    --no-header-files \
    --no-man-pages \
    --output /custom-jre

# Stage 3: Final minimal image
FROM alpine:3.19

# Install minimal dependencies for Java - FIXED: use apk update first
RUN apk update && apk add --no-cache libstdc++

# Add a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app

# Copy custom JRE and application
COPY --from=jre-builder /custom-jre /opt/java
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

ENV JAVA_HOME=/opt/java
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]