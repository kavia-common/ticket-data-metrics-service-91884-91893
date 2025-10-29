package com.example.devxdashboardbackend.exception;

/**
 * PUBLIC_INTERFACE
 * Thrown when request validation fails (e.g., missing file, invalid type).
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
