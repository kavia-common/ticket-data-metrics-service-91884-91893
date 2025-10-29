package com.example.devxdashboardbackend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Standard error response payload with code, message, and optional details.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    // PUBLIC_INTERFACE
    private String error;
    // PUBLIC_INTERFACE
    private String message;
    // PUBLIC_INTERFACE
    private List<String> details;

    public ErrorResponse() {}

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(String error, String message, List<String> details) {
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
