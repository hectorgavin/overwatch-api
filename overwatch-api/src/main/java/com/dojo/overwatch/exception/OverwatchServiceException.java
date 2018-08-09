package com.dojo.overwatch.exception;

public class OverwatchServiceException extends RuntimeException {

    public OverwatchServiceException(final String message) {
        super(message);
    }

    public OverwatchServiceException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
