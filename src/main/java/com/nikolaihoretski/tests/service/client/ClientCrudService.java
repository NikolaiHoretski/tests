package com.nikolaihoretski.tests.service.client;

import com.nikolaihoretski.tests.dto.AuthResult;
import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserCreateResponseDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import lombok.NonNull;

public interface ClientCrudService {

    @NonNull
    AuthResult create(@NonNull UserCreateRequestDto createRequestDto);

    boolean update(@NonNull UserUpdateRequestDto userDto);

    void delete();
}
