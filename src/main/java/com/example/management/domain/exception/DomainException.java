package com.example.management.domain.exception;

/**
 * Base exception for domain rule violations.
 * Domain layer has no framework dependencies.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
