# Service Endpoints

This service exposes **only** the following endpoints:

## Health Check
- **GET /health**
  - Returns: 200 OK
  - Body: "OK" (text/plain)
  - Description: Service health status check

## API Documentation
- **GET /docs**
  - Returns: 302 Found (redirect to /swagger-ui.html)
  - Description: Redirects to Swagger UI

- **GET /swagger-ui.html**
  - Returns: Swagger UI HTML page
  - Description: Interactive API documentation interface

- **GET /v3/api-docs**
  - Returns: OpenAPI JSON specification
  - Content-Type: application/json
  - Description: Complete OpenAPI 3.0 specification

- **GET /v3/api-docs/swagger-config**
  - Returns: Swagger UI configuration
  - Content-Type: application/json
  - Description: Configuration for Swagger UI

- **GET /swagger-ui/***
  - Returns: Static assets (CSS, JS, images)
  - Description: Supporting static assets for Swagger UI

## Error Handling
- **Any other unmapped path**
  - Returns: JSON error via GlobalErrorController
  - Example response (404):
    ```json
    {
      "timestamp": "2024-01-15T10:30:00Z",
      "status": 404,
      "error": "Not Found",
      "path": "/unknown-path",
      "message": "No endpoint found for this path"
    }
    ```

## Configuration Notes
- Actuator endpoints: **DISABLED**
- H2 console: **DISABLED**
- SpringDoc scans: `com.example.devxdashboardbackend` and `com.example.demo` packages only
- All application endpoints (e.g., /api/*, /upload, etc.) are **DISABLED**
- Server port: 3001
- GlobalErrorController is hidden from Swagger docs and does not interfere with Swagger assets

## Testing Endpoints
```bash
# Health check
curl http://localhost:3001/health

# API documentation redirect
curl -L http://localhost:3001/docs

# OpenAPI JSON
curl http://localhost:3001/v3/api-docs

# Swagger config
curl http://localhost:3001/v3/api-docs/swagger-config

# Test 404 for unmapped route
curl http://localhost:3001/api/anything
```
