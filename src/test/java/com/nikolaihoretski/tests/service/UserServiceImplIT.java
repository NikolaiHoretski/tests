package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.repo.PermissionRepo;
import com.nikolaihoretski.tests.repo.RoleRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
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
    void shouldReturnFindUserDtoByUsernameWhenUserExists() {

        final String username = "admin";

        final UserResponseDto result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertTrue(result.roles().contains("ROLE_ADMIN"));
        assertTrue(result.permissions().contains("READ"));
    }

    @Test
    void shouldReturnFindUserDtoByIdWhenUserExists() {

        final Long userId = 1L;

        final UserResponseDto result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());

    }

}
