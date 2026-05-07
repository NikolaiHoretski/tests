package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import com.nikolaihoretski.tests.service.client.ClientCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class ClientController {

    private final ClientCrudService clientCrudService;

    public ClientController(ClientCrudService clientCrudService) {
        this.clientCrudService = clientCrudService;
    }

    @PatchMapping("/update/me")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).USER.name())")
    public Map<String, Boolean> update(@RequestBody UserUpdateRequestDto dto) {
        boolean isUpdated = clientCrudService.update(dto);
        return Map.of("success", isUpdated);
    }

    @DeleteMapping("/delete/me")
    @PreAuthorize("hasRole(T(com.nikolaihoretski.tests.model.RoleAccess).USER.name())")
    public ResponseEntity<Void> delete() {
        clientCrudService.delete();
        return ResponseEntity.noContent().build();
    }

}
