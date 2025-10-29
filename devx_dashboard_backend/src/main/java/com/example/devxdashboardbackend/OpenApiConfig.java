package com.example.devxdashboardbackend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for the DevX Dashboard Backend service.
 * Provides metadata and server information for the API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:3001}")
    private String serverPort;

    /**
     * Configures OpenAPI documentation with service metadata.
     * 
     * @return OpenAPI configuration bean
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevX Dashboard Backend API")
                        .version("0.1.0")
                        .description("REST API service for processing ticket data and computing metrics. " +
                                "This service accepts Excel file uploads containing ticket data and returns " +
                                "computed metrics including ticket counts, SLA adherence, MTTR, and remarks.")
                        .contact(new Contact()
                                .name("DevX Team")
                                .email("support@example.com")))
                .addServersItem(new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Local development server"))
                .addServersItem(new Server()
                        .url("https://vscode-internal-35030-beta.beta01.cloud.kavia.ai:" + serverPort)
                        .description("Cloud preview server"));
    }
}
