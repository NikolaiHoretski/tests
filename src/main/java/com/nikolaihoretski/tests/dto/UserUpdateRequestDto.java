package com.nikolaihoretski.tests.dto;

public record UserUpdateRequestDto(
        Long id,
        String name,
        String password,
        String email
) {
}
