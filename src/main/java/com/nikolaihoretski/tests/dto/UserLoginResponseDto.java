package com.nikolaihoretski.tests.dto;

import java.util.UUID;

public record UserLoginResponseDto (
        UUID id,
        String username,
        String accessToken
) {
}
