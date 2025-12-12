# Backend (Swafy)

This is the Java (Spring Boot) backend for Swafy.

Build locally
- From the `backend/` directory:
  ./mvnw clean package
  java -jar target/<artifact>-SNAPSHOT.jar

Build with Docker
- From repository root:
  docker-compose -f infra/docker-compose.yml build backend

Run with Docker Compose
- Copy `infra/.env.example` to `infra/.env` and edit if needed
- docker-compose -f infra/docker-compose.yml up

Important environment variables
- SPRING_DATASOURCE_URL (e.g. jdbc:postgresql://db:5432/swafy)
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SPRING_PROFILES_ACTIVE

Health & readiness
- The docker-compose config expects the backend to expose an Actuator health endpoint at /actuator/health.
- Consider enabling Spring Boot Actuator and a readiness probe in production.

Next improvements
- Add an `application.yml` per profile with sensible defaults.
- Add a CI job to build and run tests, and produce Docker images.
- Add OpenAPI spec for the HTTP API.
