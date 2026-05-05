package com.nikolaihoretski.tests.dto;

public record DeleteUserResponseDto (
        UserResponseDto userInfo,
        Boolean isDeleted
) {
}
