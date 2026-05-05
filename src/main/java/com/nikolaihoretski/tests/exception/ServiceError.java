package com.nikolaihoretski.tests.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ServiceError extends RuntimeException {

    private final int errorCode;
    private final HttpStatus status;

    protected ServiceError(String message, int errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

}
