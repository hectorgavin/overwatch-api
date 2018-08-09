package com.dojo.overwatch.exception;

public class HeroNotFoundException extends OverwatchServiceException {

    public HeroNotFoundException(final String message) {
        super(message);
    }

    public HeroNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
