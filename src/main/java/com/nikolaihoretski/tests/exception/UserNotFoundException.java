package com.nikolaihoretski.tests.exception;

import lombok.NonNull;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(@NonNull Long id) {
    super("User with id " + id + " not found");
    }
}
