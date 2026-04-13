package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import lombok.NonNull;

public interface UserService {

    @NonNull
    UserResponseDto findByUsername(@NonNull String username);

    @NonNull
    UserResponseDto findById(@NonNull Long id);

    @NonNull
    UserResponseWithIdUsernameDto create(@NonNull UserCreateRequestDto userDto);

    @NonNull
    UserResponseWithIdUsernameDto update(@NonNull UserCreateRequestDto userDto);
}
