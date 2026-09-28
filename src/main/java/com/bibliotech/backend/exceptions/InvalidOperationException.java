package com.bibliotech.backend.exceptions;

public class InvalidOperationException extends DomainException {
    public InvalidOperationException(String message) {
        super(message);
    }
}