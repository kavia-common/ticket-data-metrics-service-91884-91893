package com.example.devxdashboardbackend.controller;

import com.example.devxdashboardbackend.model.TicketMetric;
import com.example.devxdashboardbackend.service.TicketMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Controller exposing the POST /api/tickets/upload endpoint.
 */
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Endpoints for uploading ticket Excel files and computing metrics")
public class TicketUploadController {

    private final TicketMetricsService ticketMetricsService;

    public TicketUploadController(TicketMetricsService ticketMetricsService) {
        this.ticketMetricsService = ticketMetricsService;
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            operationId = "uploadTicketExcel",
            summary = "Upload tickets Excel file",
            description = "Accepts a .xlsx file under form field 'file', validates and parses it, computes metrics, and returns a JSON array of metric objects.",
            tags = {"Tickets"},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    description = "Multipart form with a single file field named 'file'"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Metrics computed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TicketMetric.class))
                            )),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "413", description = "File too large",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "422", description = "Parsing error",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<List<TicketMetric>> upload(
            @Parameter(
                    description = "Excel .xlsx file",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("file") MultipartFile file
    ) {
        List<TicketMetric> result = ticketMetricsService.processUpload(file);
        return ResponseEntity.ok(result);
    }
}
