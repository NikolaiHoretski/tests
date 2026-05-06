package com.nikolaihoretski.tests.service.admin;

import com.nikolaihoretski.tests.dto.DeleteUserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateForAdminRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateResponseDto;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
    UserResponseDto getById(@NonNull UUID id);

    boolean createForAdmin(@NonNull UserCreateForAdminRequestDto userDto);

    void delete(@NonNull UUID id);

}
