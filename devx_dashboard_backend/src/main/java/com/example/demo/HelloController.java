package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PUBLIC_INTERFACE
 * Minimal controller exposing only the health check endpoint.
 */
@RestController
@Tag(name = "Health", description = "Service health check endpoint")
public class HelloController {

    /**
     * PUBLIC_INTERFACE
     * GET /health - Returns a simple OK payload for health checks.
     * @return "OK" when the service is up.
     */
    @GetMapping("/health")
    @Operation(
        summary = "Health Check",
        description = "Returns the service health status. Returns 'OK' when the service is operational."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service is healthy",
        content = @Content(
            mediaType = "text/plain",
            schema = @Schema(type = "string", example = "OK")
        )
    )
    public String health() {
        return "OK";
    }
}