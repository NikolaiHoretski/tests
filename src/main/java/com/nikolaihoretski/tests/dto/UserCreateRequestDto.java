package com.nikolaihoretski.tests.dto;

import java.util.Set;

public record UserCreateRequestDto(
        String username,
        String name,
        String password,
        String email,
        Set<String> roles,
        Set<String> permissions
) {
}
