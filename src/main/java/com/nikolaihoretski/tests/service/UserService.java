package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import lombok.NonNull;

public interface UserService {

    @NonNull
    UserResponseDto findByUsername(@NonNull String username);

    @NonNull
    UserResponseDto findById(@NonNull Long id);

    @NonNull
    UserResponseWithIdUsernameDto createForAdmin(@NonNull UserCreateRequestDto userDto);

    @NonNull
    UserResponseWithIdUsernameDto update(@NonNull UserUpdateRequestDto userDto);
}
