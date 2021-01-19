package com.egen.texasburger.exception;

public class CustomException extends RuntimeException {

    public CustomException() {
        super();
    }

    public CustomException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CustomException(final String message) {
        super(message);
    }

    public CustomException(final Throwable cause) {
        super(cause);
    }
}