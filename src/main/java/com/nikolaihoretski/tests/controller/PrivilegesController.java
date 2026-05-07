package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.UserPrivilegesDto;
import com.nikolaihoretski.tests.service.PrivilegeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PrivilegesController {

    private final PrivilegeService privilegeService;

    public PrivilegesController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping("/getPrivileges")
    @PreAuthorize("isAuthenticated()")
    public UserPrivilegesDto getPrivileges() {
        return privilegeService.getUserPrivileges();
    }
}
