package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Controller Exception Handler.
 *
 * @author Michael Szczepanski
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Handler for an IllegalArgumentException. Response status will be 400.
     *
     * @param ex exception thrown
     * @return error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException ex) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Handling IllegalArgumentException.", ex);
        }

        return new ErrorMessage(HttpStatus.BAD_REQUEST, ex);
    }
}

/**
 * Error Message Information.
 *
 * @author Michael Szczepanski
 */
class ErrorMessage {
    private int status;
    private String message;
    private String error;

    /**
     * Constructor for the Error Message class.
     *
     * @param status http status code to return
     * @param message exception message that was thrown
     * @param error http status error to return
     */
    public ErrorMessage(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    /**
     * Overloaded Constructor for the Error Message class.
     *
     * @param status status object
     * @param ex exception that was thrown
     */
    public ErrorMessage(HttpStatus status, Exception ex) {
        this(status.value(), ex.getMessage(), status.getReasonPhrase());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
