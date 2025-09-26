package com.ep18.couriersync.backend.common.exception;

public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super("CONFLICT", message);
    }
}