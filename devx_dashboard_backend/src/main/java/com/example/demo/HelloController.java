package com.example.devxdashboardbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PUBLIC_INTERFACE
 * Minimal controller exposing only the health check endpoint.
 */
@RestController
public class HelloController {

    /**
     * PUBLIC_INTERFACE
     * GET /health - Returns a simple OK payload for health checks.
     * @return "OK" when the service is up.
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}