package com.nikolaihoretski.tests.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public class UserNotAuthenticationException extends ServiceError {

    public UserNotAuthenticationException(@NonNull Long id) {
        super(ErrorCode.USER_NOT_AUTH.format("id", id),
                ErrorCode.USER_NOT_AUTH.getCode(),
                HttpStatus.UNAUTHORIZED);
    }

}
