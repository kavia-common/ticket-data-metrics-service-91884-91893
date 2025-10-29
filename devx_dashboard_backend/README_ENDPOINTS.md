# Service Endpoints

This service is currently hardened to expose only a simple health check.

- GET /health
  - Returns: 200 OK
  - Body: "OK"

Other behaviors:
- GET /docs
  - Returns: 410 GONE
  - Body (JSON): {"error":"DOCS_DISABLED","message":"API documentation UI is disabled on this service.","hint":"Use /health for basic availability check."}
  - Note: Swagger/OpenAPI is not enabled and no static UI is served.
- Any other path
  - Returns: JSON error via the global error handler (e.g., 404 for unknown routes).

All other endpoints are disabled or removed:
- Upload APIs removed.
- Swagger/OpenAPI disabled (no /swagger-ui.html, no /v3/api-docs).
- Actuator endpoints disabled.
- H2 console disabled.

Server continues to listen on the configured port (3001).
