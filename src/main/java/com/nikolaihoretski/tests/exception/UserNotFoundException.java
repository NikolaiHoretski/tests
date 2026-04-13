package com.nikolaihoretski.tests.exception;

import lombok.NonNull;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(@NonNull String username) {
    super("User with username " + username + " not found");
    }
}
