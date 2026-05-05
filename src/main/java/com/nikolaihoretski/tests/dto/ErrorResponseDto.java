package com.nikolaihoretski.tests.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        Integer errorCode,
        String message,
        LocalDateTime timestamp,
        String path
) {
}
