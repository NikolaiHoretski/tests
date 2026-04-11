package com.nikolaihoretski.tests.exception;

import lombok.NonNull;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(@NonNull String username) {
        super("User " + username + " already exists");
    }
}
