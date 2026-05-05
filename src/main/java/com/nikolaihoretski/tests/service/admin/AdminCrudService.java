package com.nikolaihoretski.tests.service.admin;

import com.nikolaihoretski.tests.dto.DeleteUserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AdminCrudService {

    @Nullable
    List<UserResponseDto> getAll();

    @Nullable
    List<DeleteUserResponseDto> getAllDeleteUsers();

    @Nullable
    List<UserResponseDto> getAllDisableUsers();

    @NonNull
    UserResponseDto getByUsername(@NonNull String username);

    @NonNull
    UserResponseDto getById(@NonNull Long id);

    @NonNull
    UserResponseWithIdUsernameDto createForAdmin(@NonNull UserCreateRequestDto userDto);

    void delete(@NonNull Long id);

}
