package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserDto;
import com.nikolaihoretski.tests.model.Permission;
import com.nikolaihoretski.tests.model.Role;
import com.nikolaihoretski.tests.repo.PermissionRepo;
import com.nikolaihoretski.tests.repo.RoleRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@EnableJpaAuditing
class UserServiceImplIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PermissionRepo permissionRepo;

    @Test
    void shouldReturnUserDtoWhenUserExists() {

       String username = "admin";

        UserDto result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals("admin", result.username());
        assertTrue(result.roles().contains("ROLE_ADMIN"));
        assertTrue(result.permissions().contains("READ"));
    }

    @Test
    void shouldCreateNewUserWithRolesAndPermissions() {

        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .description("Admin role")
                .userRoles(new HashSet<>())
                .rolePermissions(new HashSet<>())
                .build();
        roleRepo.save(adminRole);
        Permission permission = Permission.builder()
                .name("WRITE")
                .description("write")
                .rolePermissions(new HashSet<>())
                .build();

        UserDto dto = new UserDto(
                null,
                "horetski",
                "password123",
                "Nikolai",
                "nikolai@example.com",
                true,
                Set.of("ROLE_ADMIN"),
                Set.of("WRITE")
        );

        boolean isCreated = userService.create(dto);

        assertTrue(isCreated, "true");
        UserDto saveUser = userService.findByUsername("horetski");

        assertNotNull(saveUser);
        assertEquals("horetski", saveUser.username());
        assertTrue(saveUser.roles().contains("ROLE_ADMIN"));
        assertTrue(saveUser.permissions().contains("WRITE"));

    }


}