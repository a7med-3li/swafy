# API Design

This file documents initial API endpoints and contracts.

## Authentication

- POST /api/v1/auth/register
- POST /api/v1/auth/login
- POST /api/v1/auth/phone/send-otp // Verifies the phone number
- POST /api/v1/auth/phone/verify-otp
## Sample resource

- GET /api/v1/users/ - list all the users in the system
- GET /api/v1/users/{id} - get user by id
- DELETE /api/v1/users/{id} - delete user by id
- PATCH /api/v1/users/{id} - update user by id
- POST /api/v1/users/{id}/restore - restore deleted users (inactive) by id

Use JSON for request and response payloads and standard HTTP status codes.
