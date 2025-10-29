# Service Endpoints - Documentation Only

⚠️ **Important Notice:** This service exposes **ONLY Swagger/OpenAPI documentation endpoints**. All functional business endpoints have been disabled.

## Exposed Endpoints

### API Documentation (Swagger/OpenAPI)

- **GET /swagger-ui.html**
  - Returns: Swagger UI HTML page
  - Description: Interactive API documentation interface
  - Purpose: View and explore API specifications

- **GET /v3/api-docs**
  - Returns: OpenAPI 3.0 JSON specification
  - Content-Type: application/json
  - Description: Complete OpenAPI specification document
  - Purpose: Machine-readable API contract

- **GET /v3/api-docs/swagger-config**
  - Returns: Swagger UI configuration
  - Content-Type: application/json
  - Description: Configuration settings for Swagger UI
  - Purpose: Swagger UI initialization

- **GET /swagger-ui/***
  - Returns: Static assets (CSS, JS, images, fonts)
  - Description: Supporting static resources for Swagger UI
  - Purpose: Render Swagger UI interface

## Disabled Endpoints

❌ **The following endpoints have been removed:**

### Previously Available (Now Disabled)
- **GET /health** - Health check endpoint (REMOVED)
- **GET /docs** - Documentation redirect (REMOVED)
- **GET /api/tickets/upload** - File upload endpoint (NEVER IMPLEMENTED)
- **GET /api/tickets/metrics** - Metrics retrieval (NEVER IMPLEMENTED)
- **All other application endpoints** - (REMOVED/DISABLED)

## Error Handling

- **Any unmapped path** (e.g., `/`, `/api/*`, `/health`, `/docs`)
  - Returns: JSON error via GlobalErrorController
  - Status Code: 404 Not Found
  - Example response:
    ```json
    {
      "timestamp": "2024-01-15T10:30:00Z",
      "status": 404,
      "error": "Not Found",
      "path": "/unknown-path",
      "message": "No static resource unknown-path."
    }
    ```

## Configuration Notes

- **Purpose:** Documentation-only service
- **Actuator endpoints:** DISABLED
- **H2 console:** DISABLED
- **Business endpoints:** ALL DISABLED
- **SpringDoc scans:** `com.example.devxdashboardbackend` and `com.example.demo` packages
- **Server port:** 3001
- **GlobalErrorController:** Hidden from Swagger docs, does not interfere with documentation assets

## Testing Documentation Endpoints

```bash
# Access Swagger UI
curl http://localhost:3001/swagger-ui.html

# Get OpenAPI specification
curl http://localhost:3001/v3/api-docs

# Get Swagger configuration
curl http://localhost:3001/v3/api-docs/swagger-config

# Verify static assets are accessible
curl http://localhost:3001/swagger-ui/swagger-ui.css

# Test 404 error handling for unmapped routes
curl http://localhost:3001/health
curl http://localhost:3001/docs
curl http://localhost:3001/api/anything
```

## Expected Behavior

✅ **Accessible:**
- Swagger UI interface and all static assets
- OpenAPI JSON specification
- Swagger configuration endpoint
- JSON error responses for unmapped paths

❌ **Not Accessible:**
- Health check endpoints
- Application business logic endpoints
- Upload or data processing endpoints
- Actuator endpoints
- H2 console

## Architecture

```
Request Flow:
1. /swagger-ui.html → SpringDoc serves Swagger UI
2. /v3/api-docs → SpringDoc serves OpenAPI JSON
3. /swagger-ui/* → SpringDoc serves static assets
4. Any other path → GlobalErrorController returns 404 JSON
```

## Summary

This service configuration provides:
- ✅ Complete Swagger/OpenAPI documentation access
- ✅ Proper error handling with JSON responses
- ❌ No functional business endpoints
- ❌ No health checks or application routes

**Use Case:** API contract documentation and specification sharing without functional implementation.
