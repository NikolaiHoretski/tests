package com.nikolaihoretski.tests.dto;

import java.util.UUID;

public record UserUpdateRequestDto(
        UUID id,
        String name,
        String password,
        String email
) {
}
