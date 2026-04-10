package com.nikolaihoretski.tests.dto;

import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String password,
        String name,
        String email,
        boolean isEnabled,
        Set<String> roles,
        Set<String> permissions
) {}
