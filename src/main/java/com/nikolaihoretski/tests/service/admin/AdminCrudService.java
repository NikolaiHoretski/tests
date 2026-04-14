package com.nikolaihoretski.tests.service.admin;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import lombok.NonNull;

public interface AdminCrudService {

    @NonNull
    UserResponseDto findByUsername(@NonNull String username);

    @NonNull
    UserResponseDto findById(@NonNull Long id);

    @NonNull
    UserResponseWithIdUsernameDto createForAdmin(@NonNull UserCreateRequestDto userDto);

}
