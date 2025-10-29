# Service Endpoints

This service exposes the following endpoints:

- GET /health
  - Returns: 200 OK
  - Body: "OK"

API Documentation:
- GET /docs -> redirects to /swagger-ui.html
- GET /swagger-ui.html -> Swagger UI
- GET /v3/api-docs -> OpenAPI JSON

Other behaviors:
- Any other unmapped path
  - Returns: JSON error via the global error handler (e.g., 404 for unknown routes).

Notes:
- Actuator endpoints remain disabled.
- H2 console remains disabled.
- SpringDoc scans only the com.example.devxdashboardbackend package.

Server continues to listen on the configured port (3001).
