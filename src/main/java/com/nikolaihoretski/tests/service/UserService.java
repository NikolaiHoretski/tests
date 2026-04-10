package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserDto;

public interface UserService {

    UserDto findByUsername(String username);

}
