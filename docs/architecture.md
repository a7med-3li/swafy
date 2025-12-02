# Architecture Overview

High-level architecture for Swafy:

- Mobile client (React Native or similar)
- Backend REST API (Java/Kotlin with Gradle, or equivalent)
- PostgreSQL database
- Docker + docker-compose for local dev

Keep services small and well-documented. Use environment variables for configuration.
