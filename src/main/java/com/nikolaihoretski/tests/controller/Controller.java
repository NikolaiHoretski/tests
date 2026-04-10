package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.UserDto;
import com.nikolaihoretski.tests.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{username}")
    public UserDto findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PostMapping("/users/create")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create (@RequestBody UserDto dto) {
        return userService.create(dto);
    }

}
