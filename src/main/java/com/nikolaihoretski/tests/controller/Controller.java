package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.CreateUserDto;
import com.nikolaihoretski.tests.dto.FindUserDto;
import com.nikolaihoretski.tests.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public FindUserDto findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/userid/{id}")
    public FindUserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/users/create")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create (@RequestBody CreateUserDto dto) {
        return userService.create(dto);
    }

}
