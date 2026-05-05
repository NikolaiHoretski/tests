package com.nikolaihoretski.tests.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ServiceError {

    public UserAlreadyExistException(@NonNull String username) {
        super(ErrorCode.USER_ALREADY_EXIST.format("username", username),
                ErrorCode.USER_ALREADY_EXIST.getCode(),
                HttpStatus.ALREADY_REPORTED);
    }

}
