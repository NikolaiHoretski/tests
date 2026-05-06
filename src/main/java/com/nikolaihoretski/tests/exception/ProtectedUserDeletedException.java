package com.nikolaihoretski.tests.exception;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProtectedUserDeletedException extends ServiceError{

    public ProtectedUserDeletedException(@NotNull UUID id) {
        super(ErrorCode.USER_NOT_FOUND.format("id", id),
                ErrorCode.USER_PROTECTED.getCode(),
                HttpStatus.CONFLICT);
    }

}
