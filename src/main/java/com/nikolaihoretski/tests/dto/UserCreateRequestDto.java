package com.nikolaihoretski.tests.dto;

public record UserCreateRequestDto(
        String username,
        String password,
        String name,
        String email
) {
}
