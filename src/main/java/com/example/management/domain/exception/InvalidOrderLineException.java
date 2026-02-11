package com.example.management.domain.exception;

/**
 * Thrown when an order line violates domain invariants (e.g. quantity <= 0, negative price).
 */
public class InvalidOrderLineException extends DomainException {

    public InvalidOrderLineException(String message) {
        super(message);
    }
}
