# Ticket Data Metrics Service

A Spring Boot-based service configured to expose **only Swagger/OpenAPI documentation endpoints**.

## Important Notice

⚠️ **All functional business endpoints have been disabled.** This service serves only API documentation.

## Exposed Endpoints

This service exposes **only** the following Swagger/OpenAPI documentation endpoints:

### API Documentation
- `GET /swagger-ui.html` - Interactive Swagger UI interface
- `GET /v3/api-docs` - OpenAPI 3.0 JSON specification
- `GET /v3/api-docs/swagger-config` - Swagger UI configuration
- `GET /swagger-ui/**` - Static assets for Swagger UI (CSS, JS, images)

### Error Handling
- All other unmapped paths return JSON error responses via GlobalErrorController (e.g., 404 for unknown routes)

## What's Disabled

❌ **All application endpoints have been removed:**
- Health check endpoints (`/health`)
- Documentation redirect endpoints (`/docs`)
- Upload endpoints (`/api/tickets/upload`)
- Metrics endpoints (`/api/tickets/metrics`)
- All other business logic endpoints

❌ **Additional disabled features:**
- Actuator endpoints
- H2 console
- Any data processing functionality

## Configuration

- **Server port:** `3001`
- **SpringDoc package scanning:** Limited to `com.example.devxdashboardbackend` and `com.example.demo`
- **Error responses:** JSON format only (no Whitelabel error page)
- **GlobalErrorController:** Hidden from Swagger docs via `@Hidden` annotation

## Running the Service

```bash
cd devx_dashboard_backend
./gradlew bootRun
```

The service will start on port 3001.

## Accessing Documentation

Once the service is running:

```bash
# Open Swagger UI in browser
open http://localhost:3001/swagger-ui.html

# Get OpenAPI JSON specification
curl http://localhost:3001/v3/api-docs

# Get Swagger configuration
curl http://localhost:3001/v3/api-docs/swagger-config

# Test error handling for unmapped routes (returns 404 JSON)
curl http://localhost:3001/api/anything
```

## Project Structure

```
devx_dashboard_backend/
├── src/main/java/com/example/
│   ├── demo/
│   │   └── devxdashboardbackendApplication.java  # Main application class
│   └── devxdashboardbackend/
│       ├── GlobalErrorController.java             # Error handling (hidden from docs)
│       └── OpenApiConfig.java                     # OpenAPI configuration
└── src/main/resources/
    └── application.properties                     # Service configuration
```

## Purpose

This service configuration demonstrates a documentation-only Spring Boot application where:
- No functional business endpoints are exposed
- Only Swagger/OpenAPI documentation is accessible
- All unmapped routes return proper JSON error responses
- The OpenAPI specification is accessible for API contract sharing

## Use Cases

This configuration is useful for:
- API contract documentation without implementation
- API design review and approval workflows
- Sharing API specifications with stakeholders
- Development planning and specification validation
