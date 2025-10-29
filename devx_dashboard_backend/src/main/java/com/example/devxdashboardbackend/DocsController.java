package com.example.devxdashboardbackend;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PUBLIC_INTERFACE
 * Controller to route /docs to the Swagger UI page when docs are enabled.
 */
@RestController
public class DocsController {

    /**
     * PUBLIC_INTERFACE
     * GET /docs - Redirects to /swagger-ui.html so users can access the Swagger UI.
     *
     * @return 302 Found redirect to /swagger-ui.html
     */
    @GetMapping(path = "/docs")
    public ResponseEntity<Void> docs() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/swagger-ui.html")
                .build();
    }
}
