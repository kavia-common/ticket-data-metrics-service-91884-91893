# Service Endpoints

This service is currently hardened to expose only a simple health check.

- GET /health
  - Returns: 200 OK
  - Body: "OK"

All other endpoints are disabled or removed:
- Upload APIs removed.
- Swagger/OpenAPI disabled (no /swagger-ui.html, no /v3/api-docs, no /docs).
- Actuator endpoints disabled.
- H2 console disabled.

Server continues to listen on the configured port (3001).
