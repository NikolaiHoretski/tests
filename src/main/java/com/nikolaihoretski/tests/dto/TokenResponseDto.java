package com.nikolaihoretski.tests.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
