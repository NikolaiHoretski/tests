package com.nikolaihoretski.tests.dto;

import java.util.UUID;

public record UserAuthLoginResponseDto(
        UUID id,
        String username,
        String accessToken
) {
}
