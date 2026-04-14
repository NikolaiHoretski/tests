package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import com.nikolaihoretski.tests.service.ClientCrudService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ClientController {

    private final ClientCrudService clientCrudService;

    public ClientController(ClientCrudService clientCrudService) {
        this.clientCrudService = clientCrudService;
    }

    @PatchMapping("/update")
    public UserResponseWithIdUsernameDto update(@RequestBody UserUpdateRequestDto dto) {
        return clientCrudService.update(dto);
    }

}
