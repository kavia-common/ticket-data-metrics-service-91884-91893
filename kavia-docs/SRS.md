# SRS and API Specification: Ticket Metrics Service (Spring Boot)

## Overview
This document refines the provided Software Requirements Specification (SRS) into a developer-friendly architecture and API specification for the Ticket Metrics Service. The service exposes REST endpoints to accept an Excel file containing ticket data, compute metrics (e.g., ticket counts, SLA adherence, MTTR, and remarks), and return results. It adheres strictly to the provided SRS and does not introduce new endpoints or business rules.

## Objectives
- Provide clear, unambiguous documentation for developers and stakeholders.
- Specify API endpoints, request/response schemas, validation, error handling, and acceptance criteria.
- Restate performance and reliability targets in measurable terms.
- Document assumptions about Excel column formats and data types.
- Maintain a professional tone with precise definitions and examples.

## Scope
- In-scope:
  - Spring Boot REST API endpoints for uploading Excel files (.xlsx) and retrieving computed metrics derived from ticket data.
  - Request validation, error handling, and response payload structures.
  - Performance, reliability, and security requirements relevant to file upload and metrics computation.
- Out-of-scope:
  - UI/Frontend components.
  - Persistence beyond temporary processing needs (unless explicitly required by the SRS).
  - Authentication/authorization schemes not specified in the SRS.
  - Data transformations or analytics beyond the defined metrics.

## Architecture Overview
The service is a Spring Boot application exposing REST endpoints for:
- Uploading an Excel file (.xlsx) with ticket data.
- Returning computed metrics from the uploaded data.

Core responsibilities:
- Validate file type and size.
- Parse Excel content based on defined assumptions and column formats.
- Compute ticket metrics as specified.
- Return results in a structured JSON response with appropriate status codes and error messages.

Assumptions regarding deployment:
- Runs as a standalone Spring Boot service.
- OpenAPI/Swagger documentation may be provided for interactive API exploration.
- No external database is strictly required unless specified by the SRS; computation may be performed in-memory upon upload.

## Excel Format Assumptions
The service assumes uploads are Microsoft Excel .xlsx files containing a single primary worksheet with ticket records. Column names and their expected data types should align with the SRS. If the SRS defines specific columns, adhere to those; if only general categories are defined, use these assumptions for parsing:
- Columns and types:
  - ticket_id: string
  - created_at: datetime (ISO-8601 or Excel datetime value)
  - closed_at: datetime (nullable; ISO-8601 or Excel datetime value)
  - priority: string (e.g., P1, P2, P3, P4)
  - status: string (e.g., Open, In Progress, Resolved, Closed)
  - sla_due_at: datetime (nullable)
  - team: string (e.g., Support, Engineering)
  - assignee: string (nullable)
  - category: string (nullable)
  - remarks: string (nullable)
- Date handling:
  - If Excel serial dates are used, they must be interpreted as local timezone or UTC as per the SRS guidance. If not specified, treat them as UTC and document any conversion.
- Missing or malformed columns:
  - If required columns for metrics are missing or malformed, the service must return a validation error detailing the missing/invalid columns.
- Row validation:
  - Rows with critical data errors may be skipped or cause request rejection depending on SRS-defined behavior. If behavior is not specified, the service should return a validation error with details.

## Metrics Definitions
The service computes metrics as specified in the SRS. Representative metrics include:
- Ticket counts: total tickets and optionally counts by status, priority, category, or team if the SRS requires them.
- SLA adherence: percentage of tickets that met SLA (closed_at <= sla_due_at) among tickets with an SLA.
- MTTR (Mean Time To Resolve): average resolution time for resolved/closed tickets (closed_at - created_at).
- Remarks: may include computed notes or flags derived from raw data where specified by the SRS.

Note: Do not add metrics beyond those in the SRS. The above list should be narrowed or expanded only to match the SRS-provided metrics.

## API Endpoints

### POST /api/tickets/upload
Accepts an Excel file, validates it, parses ticket data, computes metrics, and returns results.

- Method: POST
- Path: /api/tickets/upload
- Content-Type: multipart/form-data
- Authentication: As defined in the SRS. If not specified, assume no auth required.
- Request parameters:
  - file (form-data, required): .xlsx file containing ticket data
- Constraints:
  - File type: .xlsx (Microsoft Excel Open XML)
  - Max file size: 5 MB
- Behavior:
  - Validate file presence, type, and size.
  - Parse the Excel and compute metrics as defined.
  - Return computed metrics and any parsing warnings (e.g., skipped rows) if allowed by the SRS.

Request example (multipart/form-data):
- form-data:
  - file: tickets_october.xlsx

Request example (curl):
```
curl -X POST https://{host}/api/tickets/upload \
  -H "Accept: application/json" \
  -F "file=@tickets_october.xlsx;type=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
```

Response: 200 OK (application/json)
```
{
  "summary": {
    "totalTickets": 123,
    "slaAdherencePercentage": 92.68,
    "mttrHours": 14.5
  },
  "breakdown": {
    "byStatus": {
      "Open": 10,
      "In Progress": 8,
      "Resolved": 60,
      "Closed": 45
    },
    "byPriority": {
      "P1": 5,
      "P2": 35,
      "P3": 60,
      "P4": 23
    }
  },
  "remarks": [
    "2 rows skipped due to malformed created_at",
    "1 ticket missing sla_due_at; excluded from SLA adherence calculation"
  ]
}
```

Error responses:
- 400 Bad Request
```
{
  "error": "VALIDATION_ERROR",
  "message": "Invalid file type. Only .xlsx files are supported.",
  "details": ["Provided content type: text/csv"]
}
```
- 413 Payload Too Large
```
{
  "error": "FILE_TOO_LARGE",
  "message": "File size exceeds the 5 MB limit.",
  "details": ["sizeBytes=6096123", "limitBytes=5242880"]
}
```
- 422 Unprocessable Entity
```
{
  "error": "PARSING_ERROR",
  "message": "Missing required columns: created_at, sla_due_at",
  "details": ["Sheet: Tickets", "Row validation failed"]
}
```
- 500 Internal Server Error
```
{
  "error": "INTERNAL_ERROR",
  "message": "Unexpected error during metrics computation."
}
```

Response fields:
- summary.totalTickets: integer
- summary.slaAdherencePercentage: number (float, 0–100)
- summary.mttrHours: number (float, hours)
- breakdown.byStatus: object<string, integer> (optional based on SRS)
- breakdown.byPriority: object<string, integer> (optional based on SRS)
- remarks: string[] (optional; computation notes)

### GET /api/tickets/metrics
Retrieves computed metrics. Depending on SRS design, this may:
- Return metrics based on the most recent upload for the session/context.
- Recompute or fetch cached metrics from persistent storage.

- Method: GET
- Path: /api/tickets/metrics
- Query parameters: none unless specified in the SRS.
- Authentication: As defined in the SRS. If not specified, assume no auth required.

Request example:
```
curl -X GET https://{host}/api/tickets/metrics -H "Accept: application/json"
```

Response: 200 OK (application/json)
```
{
  "summary": {
    "totalTickets": 123,
    "slaAdherencePercentage": 92.68,
    "mttrHours": 14.5
  },
  "breakdown": {
    "byStatus": {
      "Open": 10,
      "In Progress": 8,
      "Resolved": 60,
      "Closed": 45
    },
    "byPriority": {
      "P1": 5,
      "P2": 35,
      "P3": 60,
      "P4": 23
    }
  },
  "remarks": [
    "Metrics derived from last uploaded file: tickets_october.xlsx"
  ]
}
```

Error responses:
- 404 Not Found
```
{
  "error": "NOT_FOUND",
  "message": "No metrics available. Upload an Excel file first."
}
```
- 500 Internal Server Error
```
{
  "error": "INTERNAL_ERROR",
  "message": "Failed to retrieve computed metrics."
}
```

## Request and Response Schema Tables

### POST /api/tickets/upload (multipart/form-data)
- Request parameters:
  - file: required, type=File (.xlsx), maxSize=5MB

- Success response (200):
  - summary.totalTickets: integer
  - summary.slaAdherencePercentage: number
  - summary.mttrHours: number
  - breakdown.byStatus: object<string, integer> (if defined by SRS)
  - breakdown.byPriority: object<string, integer> (if defined by SRS)
  - remarks: array<string> (optional)

- Error responses:
  - 400 VALIDATION_ERROR
  - 413 FILE_TOO_LARGE
  - 422 PARSING_ERROR
  - 500 INTERNAL_ERROR

### GET /api/tickets/metrics
- Request parameters: none
- Success response (200):
  - Same schema as POST /api/tickets/upload success response.
- Error responses:
  - 404 NOT_FOUND
  - 500 INTERNAL_ERROR

## Validation Rules
- File must be present in form field named file.
- File must be of type .xlsx with content type application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.
- File size must not exceed 5 MB (5,242,880 bytes).
- Excel sheet must include required columns per the SRS. If required columns are missing or contain malformed data, respond with 422 and details.
- Date fields must be parsable as valid date/time per SRS. Rows with invalid dates may cause validation failure or be reported in remarks, according to SRS behavior.
- If the SRS specifies allowed values for priority/status, validate enumerations and report invalid values in details.

## Error Handling
- Error response structure:
```
{
  "error": "<ERROR_CODE>",
  "message": "<Human-readable error message>",
  "details": ["<optional additional details>"]
}
```

- Error codes:
  - VALIDATION_ERROR: input or file validation failed.
  - FILE_TOO_LARGE: uploaded file exceeds size limit.
  - PARSING_ERROR: Excel parsing failed due to format or content issues.
  - NOT_FOUND: requested metrics not available yet.
  - INTERNAL_ERROR: unexpected server error.

- HTTP status mapping:
  - 400: VALIDATION_ERROR
  - 413: FILE_TOO_LARGE
  - 422: PARSING_ERROR
  - 404: NOT_FOUND
  - 500: INTERNAL_ERROR

## Security Requirements
- Enforce content-type and extension checks for uploads.
- Reject files that do not match .xlsx type.
- Apply size limits at controller/filter and container levels to prevent resource exhaustion.
- If authentication is required by the SRS, implement appropriate headers or OAuth/JWT schemes. If not specified, do not add additional security requirements.

## Performance and Reliability Requirements
- File size limit: 5 MB maximum per upload.
- Parsing and metrics computation:
  - Aim for processing completion within a target time window consistent with the SRS (e.g., within a few seconds for 5 MB files).
  - Memory usage should remain within typical application container limits for the file size.
- Concurrency:
  - Service should handle concurrent uploads within typical backend throughput for the specified environment.
- Availability:
  - If the SRS specifies an availability target, adhere to it. Otherwise, ensure graceful error handling and meaningful error responses.

## Acceptance Criteria
- Upload Endpoint:
  - Accepts a .xlsx file up to 5 MB and rejects other types or larger files with correct error codes.
  - Validates required columns and returns detailed errors when invalid.
  - Computes metrics (ticket counts, SLA adherence, MTTR, and remarks) according to SRS definitions.
  - Returns a structured JSON response including summary metrics and optional breakdowns as per SRS.
- Metrics Endpoint:
  - Returns the latest computed metrics per SRS definition or a 404 if none are available.
- Error Handling:
  - Consistent error payloads with error, message, and details fields.
  - Appropriate HTTP status codes per error scenario.
- Performance:
  - Processes valid 5 MB .xlsx uploads within the target performance window defined by the SRS.
- Documentation:
  - Endpoints, schemas, validation rules, and error codes are documented and match runtime behavior.

## Sample Payloads and Responses

### Sample Excel Assumptions
- Sheet: "Tickets"
- Columns: ticket_id, created_at, closed_at, priority, status, sla_due_at, team, assignee, category, remarks
- Example rows:
  - TCK-1001, 2024-10-01T10:00:00Z, 2024-10-02T09:30:00Z, P2, Closed, 2024-10-03T00:00:00Z, Support, jdoe, Incident, ""
  - TCK-1002, 2024-10-03T08:15:00Z, , P3, Open, 2024-10-05T00:00:00Z, Engineering, asmith, ServiceRequest, "awaiting parts"

### Upload Response (200)
```
{
  "summary": {
    "totalTickets": 2,
    "slaAdherencePercentage": 100.0,
    "mttrHours": 23.5
  },
  "breakdown": {
    "byStatus": {
      "Open": 1,
      "Closed": 1
    },
    "byPriority": {
      "P2": 1,
      "P3": 1
    }
  },
  "remarks": []
}
```

### Metrics Response (404)
```
{
  "error": "NOT_FOUND",
  "message": "No metrics available. Upload an Excel file first."
}
```

## Logging and Observability
- Log upload requests including filename and size (without persisting contents).
- Log validation and parsing errors with sufficient detail to diagnose issues without leaking sensitive data.
- Expose standard health and info endpoints if enabled by the platform for basic service checks.

## Future Enhancements (Optional)
- Support additional file formats (e.g., CSV) if permitted by future requirements.
- Pagination or filtering of results when multiple datasets are stored.
- Additional breakdowns and time-series analytics if requested by stakeholders.
- Authentication and authorization integration if required.

## Change Management
This document reflects the current SRS. Any changes to endpoint structures, validation rules, or performance targets should be synchronized with the codebase and versioned alongside releases.

## References
- Provided SRS for Ticket Metrics Service (source of truth).
- Spring Boot REST API patterns (implementation framework).
- Excel .xlsx file format constraints and parsing best practices.
