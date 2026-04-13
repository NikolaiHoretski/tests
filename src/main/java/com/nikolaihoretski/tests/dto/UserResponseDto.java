package com.nikolaihoretski.tests.dto;

import java.util.Set;

public record UserResponseDto(
        Long id,
        String username,
        String name,
        String email,
        Boolean isEnabled,
        Set<String> roles,
        Set<String> permissions
) {
}
