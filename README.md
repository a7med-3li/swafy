# Swafy

Swafy is a ride‑sharing system. This repository is a starter scaffold containing:

- `backend/` - Maven-based Java backend (Spring Boot)
- `mobile/` - mobile client (placeholder)
- `infra/` - docker-compose and infra scripts for local development
- `docs/` - project documentation (API notes, architecture)

Quick summary
- Purpose: prototype and iterate on a ride-sharing backend and mobile client.
- Language: Java backend (Spring Boot), mobile client (platform TBD).
- Local development: DB + backend run via docker-compose; backend can also be built with the included Maven wrapper.

Getting started (local development)
1. Copy infra/.env.example to infra/.env and customize if necessary.
2. Start DB and backend:
   docker-compose -f infra/docker-compose.yml up --build
3. Build the backend jar locally (optional):
   cd backend
   ./mvnw clean package

Recommended next improvements
- Add automated tests (unit + integration) and a CI pipeline.
- Add a Dockerfile for the mobile client or provide an emulator script.
- Add API contract specs (OpenAPI/Swagger) in docs/ and generate server/client stubs.
- Add observability (metrics, structured logging, tracing).

Repository structure
- backend/: Java service (Spring Boot)
- mobile/: mobile client work in progress
- infra/: docker-compose.yml, env examples and helper scripts
- docs/: architecture and design notes

Links
- Repo: https://github.com/a7med-3li/swafy
