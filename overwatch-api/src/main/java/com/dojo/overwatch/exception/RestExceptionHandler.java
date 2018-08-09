package com.dojo.overwatch.exception;

import com.dojo.overwatch.common.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(HeroNotFoundException.class)
    public ResponseEntity<Object> handleHeroNotFound(final HeroNotFoundException e) {
        return buildResponseEntity(NOT_FOUND, e);
    }

    @ResponseBody
    @ExceptionHandler(AbilityNotFoundException.class)
    public ResponseEntity<Object> handlerAbilityNotFound(final AbilityNotFoundException e) {
        return buildResponseEntity(NOT_FOUND, e);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handlerIllegalArgumentException(final IllegalArgumentException e) {
        return buildResponseEntity(BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(final HttpStatus status, final OverwatchServiceException e) {
        return new ResponseEntity<>(new ApiError(status.value(), e.getMessage()), status);
    }

    private ResponseEntity<Object> buildResponseEntity(final HttpStatus status, final String message) {
        return new ResponseEntity<>(new ApiError(status.value(), message), status);
    }

}