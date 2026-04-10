package com.nikolaihoretski.tests.dto;

import java.util.Set;

public record UserDto(
        String username,
        String password,
        String name,
        String email,
        Boolean isEnabled,
        Set<String> roles,
        Set<String> permissions
) {}
