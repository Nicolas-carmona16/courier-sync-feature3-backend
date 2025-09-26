package com.ep18.couriersync.backend.common.exception;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
