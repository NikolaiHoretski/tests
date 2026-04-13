package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import lombok.NonNull;

public interface UserService {

    @NonNull
    UserResponseDto findByUsername(@NonNull String username);

    @NonNull
    UserResponseDto findById(@NonNull Long id);

    boolean create(@NonNull UserCreateRequestDto userDto);
}
