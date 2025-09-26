package com.ep18.couriersync.backend.common.exception;

public abstract class DomainException extends RuntimeException {
    private final String code;

    protected DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() { return code; }
}