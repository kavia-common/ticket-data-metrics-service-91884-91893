package com.example.devxdashboardbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/**
 * PUBLIC_INTERFACE
 * OpenAPI configuration providing application-level metadata and tags for the API docs.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ticket Data Metrics Service",
                version = "0.1.0",
                description = "REST API that accepts Excel files and returns processed ticket metrics.",
                contact = @Contact(name = "DevX Dashboard", url = "https://example.com", email = "support@example.com"),
                license = @License(name = "Apache-2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = {
                @Server(url = "/", description = "Default server")
        },
        tags = {
                @Tag(name = "Tickets", description = "Endpoints for uploading ticket Excel files and computing metrics"),
                @Tag(name = "Hello Controller", description = "Basic endpoints for devxdashboardbackend")
        }
)
public class OpenApiConfig {
    // Intentionally empty - annotations drive the OpenAPI meta.
    // Keep minimal supported annotations to avoid classpath issues during OpenAPI generation.
}
