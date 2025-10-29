package com.example.devxdashboardbackend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PUBLIC_INTERFACE
 * Controller to route /docs to the Swagger UI page when docs are enabled.
 */
@RestController
@Tag(name = "Documentation", description = "API documentation endpoints")
public class DocsController {

    /**
     * PUBLIC_INTERFACE
     * GET /docs - Redirects to /swagger-ui.html so users can access the Swagger UI.
     *
     * @return 302 Found redirect to /swagger-ui.html
     */
    @GetMapping(path = "/docs")
    @Operation(
        summary = "API Documentation",
        description = "Redirects to the Swagger UI page for interactive API documentation"
    )
    @ApiResponse(responseCode = "302", description = "Redirect to Swagger UI")
    public ResponseEntity<Void> docs() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/swagger-ui.html")
                .build();
    }
}
