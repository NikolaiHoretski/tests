package com.nikolaihoretski.tests.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends ServiceError {

    public UserNotFoundException(@NonNull UUID id) {
    super(ErrorCode.USER_NOT_FOUND.format("id", id),
            ErrorCode.USER_NOT_FOUND.getCode(),
            HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(@NonNull String username) {
        super(ErrorCode.USER_NOT_FOUND.format("username", username),
                ErrorCode.USER_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND);
    }

}
