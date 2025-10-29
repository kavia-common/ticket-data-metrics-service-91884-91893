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
