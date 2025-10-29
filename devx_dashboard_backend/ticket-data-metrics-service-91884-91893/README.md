# Ticket Data Metrics Service

A Spring Boot-based REST API service configured to expose only health check and API documentation endpoints.

## Exposed Endpoints

This service exposes **only** the following endpoints:

### Health Check
- `GET /health` - Service health status check

### API Documentation
- `GET /docs` - Redirects to Swagger UI
- `GET /swagger-ui.html` - Interactive Swagger UI
- `GET /v3/api-docs` - OpenAPI 3.0 JSON specification
- `GET /v3/api-docs/swagger-config` - Swagger UI configuration
- `GET /swagger-ui/**` - Static assets for Swagger UI

### Error Handling
- All other unmapped paths return JSON error responses via GlobalErrorController (e.g., 404 for unknown routes)

## What's Disabled
- All application endpoints (e.g., `/api/*`, `/upload`, etc.)
- Actuator endpoints
- H2 console
- Any upload or data processing endpoints

## Configuration
- Server port: `3001`
- SpringDoc package scanning: Limited to `com.example.devxdashboardbackend` and `com.example.demo`
- Error responses: JSON format only (no Whitelabel error page)

## Running the Service

```bash
cd devx_dashboard_backend
./gradlew bootRun
```

## Testing

```bash
# Health check
curl http://localhost:3001/health

# View API docs
open http://localhost:3001/swagger-ui.html

# Get OpenAPI spec
curl http://localhost:3001/v3/api-docs

# Test error handling (404)
curl http://localhost:3001/api/nonexistent
```

## Project Structure
```
devx_dashboard_backend/
├── src/main/java/com/example/
│   ├── demo/
│   │   ├── HelloController.java           # /health endpoint
│   │   └── devxdashboardbackendApplication.java
│   └── devxdashboardbackend/
│       ├── DocsController.java            # /docs redirect
│       ├── GlobalErrorController.java     # Error handling
│       └── OpenApiConfig.java             # OpenAPI configuration
└── src/main/resources/
    └── application.properties             # Service configuration
```

## Documentation
For detailed endpoint specifications, see [README_ENDPOINTS.md](devx_dashboard_backend/README_ENDPOINTS.md)
