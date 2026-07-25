# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copy maven wrapper and pom first for better caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw || true

# Download dependencies to cache them in a separate Docker layer
RUN ./mvnw dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN ./mvnw -B -DskipTests clean package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Create a non-root user
RUN addgroup --system app && adduser --system --ingroup app app

# Copy jar produced by build stage with explicit ownership
# Note: Ensure this wildcard only matches ONE file.
# This will catch swafy-1.0.0.jar, swafy-0.0.1-SNAPSHOT.jar, etc.
COPY --from=builder --chown=app:app /workspace/target/*.jar app.jar

USER app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
