package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.service.admin.AdminCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminCrudService adminCrudService;

    public AdminController(AdminCrudService adminCrudService) {
        this.adminCrudService = adminCrudService;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.dto.RoleAccess).ADMIN.name())")
    public UserResponseDto findByUsername(@PathVariable String username) {
        return adminCrudService.findByUsername(username);
    }

    @GetMapping("/userid/{id}")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.dto.RoleAccess).ADMIN.name())")
    public UserResponseDto findById(@PathVariable Long id) {
        return adminCrudService.findById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.dto.RoleAccess).ADMIN.name())")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseWithIdUsernameDto create(@RequestBody UserCreateRequestDto dto) {
        return adminCrudService.createForAdmin(dto);
    }

}
