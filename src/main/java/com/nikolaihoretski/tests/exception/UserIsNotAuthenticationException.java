package com.nikolaihoretski.tests.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public class UserIsNotAuthenticationException extends ServiceError {

    public UserIsNotAuthenticationException(@NonNull Long id) {
        super(ErrorCode.USER_NOT_AUTH.format("id", id),
                ErrorCode.USER_NOT_AUTH.getCode(),
                HttpStatus.UNAUTHORIZED);
    }

    public UserIsNotAuthenticationException(@NonNull String username) {
        super(ErrorCode.USER_NOT_AUTH.format("username", username),
                ErrorCode.USER_NOT_AUTH.getCode(),
                HttpStatus.UNAUTHORIZED);
    }

    public UserIsNotAuthenticationException() {
        super("User is not authenticated",
                ErrorCode.USER_NOT_AUTH.getCode(),
                HttpStatus.UNAUTHORIZED);
    }

}
