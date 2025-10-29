package com.example.devxdashboardbackend.controller;

import com.example.devxdashboardbackend.model.TicketMetric;
import com.example.devxdashboardbackend.service.TicketMetricsService;
import io.swagger.v3.oas.annotations.Operation;

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
            summary = "Upload tickets Excel file"
    )
    public ResponseEntity<List<TicketMetric>> upload(
            @RequestPart("file") MultipartFile file
    ) {
        List<TicketMetric> result = ticketMetricsService.processUpload(file);
        return ResponseEntity.ok(result);
    }
}
