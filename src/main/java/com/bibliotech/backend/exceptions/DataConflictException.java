package com.bibliotech.backend.exceptions;

public class DataConflictException extends DomainException {
    public DataConflictException(String message) {
        super(message);
    }
}