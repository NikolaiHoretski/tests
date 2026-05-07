package com.nikolaihoretski.tests.dto;

import java.util.UUID;

public record UserAuthRefreshResponseDto(
        UUID id,
        String accessToken
) {
}
