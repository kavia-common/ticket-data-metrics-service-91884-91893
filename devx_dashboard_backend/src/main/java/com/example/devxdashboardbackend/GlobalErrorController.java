package com.example.devxdashboardbackend;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PUBLIC_INTERFACE
 * Global error controller to provide JSON responses for all errors, including
 * unmapped paths (404), preventing the default Whitelabel error page.
 * 
 * Important: This controller ONLY handles /error endpoint and does NOT intercept:
 * - /swagger-ui.html and /swagger-ui/** (Swagger UI and static assets)
 * - /v3/api-docs and /v3/api-docs/** (OpenAPI JSON and configuration)
 * - /health and /docs (application endpoints)
 * 
 * These paths are served directly by SpringDoc and application controllers.
 * This controller is hidden from OpenAPI documentation via @Hidden annotation.
 */
@org.springframework.web.bind.annotation.RestController
@Hidden
public class GlobalErrorController implements ErrorController {

    /**
     * PUBLIC_INTERFACE
     * Handles all errors routed to /error and returns a JSON payload with the status code and message.
     * The HTTP status is derived from standard servlet request attributes.
     *
     * @param request The HttpServletRequest carrying error attributes.
     * @return ResponseEntity with application/json body and appropriate HTTP status.
     */
    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = (statusAttr instanceof Integer) ? (Integer) statusAttr : 500;
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", uri != null ? uri : "");
        // Prefer explicit message, fall back to throwable class/message for debugging
        if (message != null && !message.isBlank()) {
            body.put("message", message);
        } else if (throwable != null) {
            body.put("message", throwable.getClass().getSimpleName() + (throwable.getMessage() != null ? (": " + throwable.getMessage()) : ""));
        } else {
            body.put("message", "Unexpected error");
        }

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
