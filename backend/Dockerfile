# Simple Dockerfile for the backend service
# Build the project outside (e.g. `./gradlew build`), then build this image
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy the fat jar (Maven puts artifacts into target/)
# This expects you to run `mvn package` which produces target/*.jar
COPY target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
