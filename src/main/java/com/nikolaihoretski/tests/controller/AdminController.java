package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.DeleteUserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.service.admin.AdminCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminCrudService adminCrudService;

    public AdminController(AdminCrudService adminCrudService) {
        this.adminCrudService = adminCrudService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminCrudService.getAll());
    }

    @GetMapping("/getAllDeletedUsers")
    public ResponseEntity<List<DeleteUserResponseDto>> getAllDeleteUsers() {
        return ResponseEntity.ok(adminCrudService.getAllDeleteUsers());
    }

    @GetMapping("/getAllDisabledUsers")
    public ResponseEntity<List<UserResponseDto>> getAllDisabledUsers() {
        return ResponseEntity.ok(adminCrudService.getAllDisableUsers());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    public UserResponseDto getByUsername(@PathVariable String username) {
        return adminCrudService.getByUsername(username);
    }

    @GetMapping("/userid/{id}")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    public UserResponseDto getById(@PathVariable Long id) {
        return adminCrudService.getById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseWithIdUsernameDto create(@RequestBody UserCreateRequestDto dto) {
        return adminCrudService.createForAdmin(dto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
