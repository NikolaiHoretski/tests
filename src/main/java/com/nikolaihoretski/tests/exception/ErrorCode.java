package com.nikolaihoretski.tests.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(40421, "User with %s %s not found"),
    USER_ALREADY_EXIST(40422, "User with %s %s already exists"),
    USER_NOT_AUTH(40423, "User with %s %s not authentication");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String format(@NonNull Object type, @NonNull Object value) {
        return String.format(message, type, value);
    }

}
