# Swafy

Starter repository scaffold for the Swafy project.

Structure:

- `backend/` - backend service (Maven-based Java app)
- `mobile/` - mobile client
- `infra/` - docker-compose and infra scripts
- `docs/` - project documentation

Getting started (local development):

1. Start the DB and backend via docker-compose:

   docker-compose -f infra/docker-compose.yml up --build

2. Build the backend jar (in `backend/`):

   ./mvnw clean package

Notes:

- Replace placeholder content with real implementation details.
- See files in `docs/` for API and MVP notes.
