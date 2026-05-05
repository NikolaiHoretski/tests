package com.nikolaihoretski.tests.service.client;

import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import lombok.NonNull;

public interface ClientCrudService {

    @NonNull
    UserResponseWithIdUsernameDto update(@NonNull UserUpdateRequestDto userDto);

    void delete();
}
