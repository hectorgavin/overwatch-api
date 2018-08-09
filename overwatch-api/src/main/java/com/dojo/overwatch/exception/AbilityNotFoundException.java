package com.dojo.overwatch.exception;

public class AbilityNotFoundException extends OverwatchServiceException {

    public AbilityNotFoundException(final String message) {
        super(message);
    }

    public AbilityNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
