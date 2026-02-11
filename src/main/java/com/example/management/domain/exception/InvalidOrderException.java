package com.example.management.domain.exception;

/**
 * Thrown when an order violates domain invariants (e.g. empty lines, total mismatch).
 */
public class InvalidOrderException extends DomainException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
