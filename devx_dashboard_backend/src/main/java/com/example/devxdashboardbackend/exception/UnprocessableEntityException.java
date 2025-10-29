package com.example.devxdashboardbackend.exception;

/**
 * PUBLIC_INTERFACE
 * Thrown when the uploaded file cannot be parsed or is semantically invalid.
 */
public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String message) { super(message); }
}
