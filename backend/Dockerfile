# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /workspace
# Copy maven wrapper and pom first for better caching if present
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# If there are submodules or the backend is packaged, copy everything and build
COPY src ./src
RUN chmod +x mvnw || true
RUN ./mvnw -B -DskipTests clean package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy jar produced by build stage (adjust the jar name as needed)
COPY --from=builder /workspace/target/*-SNAPSHOT.jar app.jar
# Use a non-root user
RUN addgroup --system app && adduser --system --ingroup app app
USER app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
