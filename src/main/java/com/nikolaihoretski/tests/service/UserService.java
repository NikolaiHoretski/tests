package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.CreateUserDto;
import com.nikolaihoretski.tests.dto.FindUserDto;
import lombok.NonNull;

public interface UserService {

    @NonNull
    FindUserDto findByUsername(@NonNull String username);

    @NonNull
    FindUserDto findById(@NonNull Long id);

    boolean create(@NonNull CreateUserDto userDto);
}
