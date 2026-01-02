# Configuration Guide

This document explains how to set up and configure the Swafy Backend application.

## Quick Start

### 1. Environment Setup

Copy the `.env.example` file to `.env` and fill in your actual values:

```bash
cp .env.example .env
```

Edit `.env` with your actual configuration values (database credentials, API keys, etc.).

### 2. Profile Configurations

The application supports multiple Spring profiles:

- **dev**: Development environment with detailed logging and H2/PostgreSQL
- **prod**: Production environment with optimized settings and security
- **test**: Test environment with in-memory H2 database

Configuration files are located in `src/main/resources/`:

- `application.properties` - Base configuration
- `application-dev.yml` - Development profile
- `application-prod.yml` - Production profile
- `application-test.yml` - Test profile

### 3. Running the Application

#### Development Mode

```bash
# Using Maven with dev profile
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or set environment variable
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run
```

#### Production Mode

```bash
# Build JAR
./mvnw clean package

# Run with production profile
export SPRING_PROFILES_ACTIVE=prod
java -jar target/backend-0.1.0.jar
```

#### Test Mode

```bash
./mvnw test -Dspring.profiles.active=test
```

### 4. Database Setup (Development)

#### PostgreSQL Setup

```bash
# Create database
createdb swafy_dev
createuser swafy_user
psql -c "ALTER USER swafy_user WITH PASSWORD 'dev_password';"
psql -c "GRANT ALL PRIVILEGES ON DATABASE swafy_dev TO swafy_user;"
```

#### Using Docker

```bash
docker run --name swafy-postgres \
  -e POSTGRES_DB=swafy_dev \
  -e POSTGRES_USER=swafy_user \
  -e POSTGRES_PASSWORD=dev_password \
  -p 5432:5432 \
  -d postgres:15-alpine
```

### 5. Configuration Priority

Environment variables override property files. The priority is:

1. System environment variables (`SPRING_DATASOURCE_URL`, etc.)
2. `.env` file (if using env file loader)
3. Profile-specific YAML files (`application-{profile}.yml`)
4. Default `application.properties`

### 6. Key Configuration Properties

#### Database

```yaml
spring.datasource.url: JDBC connection string
spring.datasource.username: Database user
spring.datasource.password: Database password
spring.jpa.hibernate.ddl-auto: create-drop|create|update|validate
```

#### JWT

```yaml
jwt.secret: Secret key for signing tokens (min 256 bits recommended)
jwt.expiration-ms: Token expiration time in milliseconds
jwt.refresh-expiration-ms: Refresh token expiration time
```

#### CORS

```yaml
cors.allowed-origins: Comma-separated list of allowed origins
cors.allowed-methods: Allowed HTTP methods
cors.allow-credentials: Allow credentials in requests
```

#### Redis (Caching)

```yaml
spring.redis.host: Redis server hostname
spring.redis.port: Redis server port
spring.redis.password: Redis password (if required)
```

#### Application-Specific

```yaml
app.ride.base-fare: Base ride fare
app.ride.per-km-rate: Rate per kilometer
app.ride.per-minute-rate: Rate per minute
app.ride.surge-multiplier: Surge pricing multiplier
app.driver.min-rating: Minimum driver rating required
```

### 7. Logging Configuration

Logs are configured per profile:

- **Development**: Detailed console and file logging (logs/swafy-dev.log)
- **Production**: Minimal console logging, detailed file logging (/var/log/swafy/swafy.log)
- **Test**: Console and in-memory logging

Configure in `application-{profile}.yml`:

```yaml
logging:
  level:
    root: INFO
    com.swafy: DEBUG
  file:
    name: logs/swafy.log
    max-size: 10MB
```

### 8. Actuator Endpoints

Spring Boot Actuator provides monitoring endpoints:

- Health: `http://localhost:8080/api/actuator/health`
- Metrics: `http://localhost:8080/api/actuator/metrics`
- Info: `http://localhost:8080/api/actuator/info`

Access control is configured in security config. In production, only health is exposed by default.

### 9. Security Considerations

**IMPORTANT**: Never commit sensitive data:

- `.env` files (use `.env.example` as template)
- Database passwords
- API keys
- JWT secrets

These should always be provided via:

1. Environment variables (recommended for production)
2. Secrets management systems (HashiCorp Vault, AWS Secrets Manager, etc.)
3. Docker secrets / Kubernetes secrets

### 10. Swagger/OpenAPI Documentation

The API documentation is available at:

- **Development/Test**: `http://localhost:8080/api/swagger-ui.html`
- **Production**: Disabled by default (can be enabled via configuration)

### 11. Docker Deployment

Build and run with Docker:

```bash
# Build Docker image
docker build -f Dockerfile -t swafy-backend:latest .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/swafy \
  -e SPRING_DATASOURCE_USERNAME=swafy_user \
  -e SPRING_DATASOURCE_PASSWORD=secure_password \
  --name swafy-backend \
  swafy-backend:latest
```

### 12. Environment Variables Reference

Key environment variables that can override configuration:

```
SPRING_PROFILES_ACTIVE=dev|prod|test
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/swafy
SPRING_DATASOURCE_USERNAME=swafy_user
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=your_secret_key
CORS_ALLOWED_ORIGINS=http://localhost:3000
SERVER_PORT=8080
LOGGING_LEVEL_COM_SWAFY=DEBUG
```

### 13. Troubleshooting

**Connection refused to database:**

- Ensure PostgreSQL is running
- Check database URL, username, and password
- Verify network connectivity

**Port already in use:**

- Change `SERVER_PORT` environment variable
- Or kill the process using the port

**JWT token errors:**

- Ensure `JWT_SECRET` is set and consistent
- Check token expiration times
- Verify token format in requests

**Logging not showing:**

- Check `LOGGING_LEVEL_COM_SWAFY` is set correctly
- Verify log file path has write permissions
- Check active profile is correct

---

For more information, see the main README.md in the project root.
