package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserServiceImplTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnUserDtoWhenUserExists() {

       String username = "admin";

        UserDto result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals("admin", result.username());
        assertTrue(result.roles().contains("ROLE_ADMIN"));
        assertTrue(result.permissions().contains("READ"));
    }

}