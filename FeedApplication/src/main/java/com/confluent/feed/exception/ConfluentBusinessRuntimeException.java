package com.confluent.feed.exception;

public class ConfluentBusinessRuntimeException extends RuntimeException {

    private ValidationError validationError;

    public ConfluentBusinessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfluentBusinessRuntimeException(String message) {
        super(message);
    }

    public ConfluentBusinessRuntimeException(Throwable cause) {
        super(cause);
    }

    public ConfluentBusinessRuntimeException(ValidationError validationError) {
        super(validationError != null ? validationError.toStringDefault() : null);
        this.validationError = validationError;
    }

    public ConfluentBusinessRuntimeException(ValidationError validationError, Throwable cause) {
        super(validationError != null ? validationError.toStringDefault() : null, cause);
        this.validationError = validationError;
    }

    public ValidationError getValidationError() {
        return validationError;
    }
}
