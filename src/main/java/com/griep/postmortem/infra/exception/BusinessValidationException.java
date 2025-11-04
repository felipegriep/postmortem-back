package com.griep.postmortem.infra.exception;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(final String message) {
        super(message);
    }
}
