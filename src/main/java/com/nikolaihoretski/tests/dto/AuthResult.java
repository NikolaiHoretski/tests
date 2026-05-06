package com.nikolaihoretski.tests.dto;

import java.util.UUID;

public record AuthResult(
        UUID id,
        String username,
        String accessToken,
        String refreshToken
) {
}
