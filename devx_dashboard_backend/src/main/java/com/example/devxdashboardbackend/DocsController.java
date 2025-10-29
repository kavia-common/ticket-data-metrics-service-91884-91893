package com.example.devxdashboardbackend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PUBLIC_INTERFACE
 * Minimal controller to handle requests to /docs without re-enabling Swagger.
 * Returns a JSON response indicating that documentation UI is disabled.
 */
@RestController
public class DocsController {

    /**
     * PUBLIC_INTERFACE
     * GET /docs - Returns a simple JSON stating that documentation is disabled.
     * Does not attempt to serve static resources or Swagger UI.
     *
     * @return ResponseEntity with 410 GONE and a small JSON body.
     */
    @GetMapping(path = "/docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> docs() {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "DOCS_DISABLED");
        body.put("message", "API documentation UI is disabled on this service.");
        body.put("hint", "Use /health for basic availability check.");
        return ResponseEntity.status(HttpStatus.GONE).body(body);
    }
}
