# Ticket Metrics Upload Endpoint

- POST /api/tickets/upload
  - Content-Type: multipart/form-data
  - Field: file (Excel .xlsx)
  - Returns: 200 OK with JSON array of TicketMetric objects.
  - Errors:
    - 400 Validation error (invalid type, missing file, >5MB when not intercepted)
    - 413 File too large (when intercepted by multipart limits)
    - 422 Parsing error (malformed/missing required columns)
    - 500 Internal error

See Swagger UI at /swagger-ui.html for interactive docs.
OpenAPI JSON is available at /v3/api-docs (used by reverse proxies and clients).
Additionally, /docs redirects to Swagger UI while preserving scheme/host/port behind proxies.

Troubleshooting:
- If /v3/api-docs returns 500, verify springdoc-openapi-starter-webmvc-ui version matches Spring Boot, and ensure annotations in controllers are minimal. This project relies on SpringDoc inference with @Operation(summary) to avoid complex schema issues.
- Confirm application.properties sets: springdoc.api-docs.path=/v3/api-docs and springdoc.swagger-ui.path=/swagger-ui.html
- Limit scanning via: springdoc.packages-to-scan=com.example.devxdashboardbackend
