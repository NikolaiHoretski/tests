package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserDto;
import lombok.NonNull;

public interface UserService {

    UserDto findByUsername(@NonNull String username);

    boolean create(@NonNull UserDto userDto);
}
