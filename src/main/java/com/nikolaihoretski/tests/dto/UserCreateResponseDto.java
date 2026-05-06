package com.nikolaihoretski.tests.dto;

import java.util.UUID;

public record UserCreateResponseDto(
        UUID id,
        String username,
        String accessToken
) {
}
