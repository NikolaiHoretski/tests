package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.DeleteUserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateForAdminRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.service.admin.AdminCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminCrudService adminCrudService;

    public AdminController(AdminCrudService adminCrudService) {
        this.adminCrudService = adminCrudService;
    }

    @GetMapping("/getAllValidUsers")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminCrudService.getAll());
    }

    @GetMapping("/getAllDeletedUsers")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    public ResponseEntity<List<DeleteUserResponseDto>> getAllDeleteUsers() {
        return ResponseEntity.ok(adminCrudService.getAllDeleteUsers());
    }

    @GetMapping("/getAllDisabledUsers")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
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
    public UserResponseDto getById(@PathVariable UUID id) {
        return adminCrudService.getById(id);
    }

    @PostMapping("/createNewUser")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Boolean> create(@RequestBody UserCreateForAdminRequestDto dto) {
        final boolean isCrated = adminCrudService.createForAdmin(dto);
        return Map.of("success", isCrated);
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).ADMIN.name())")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        adminCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
