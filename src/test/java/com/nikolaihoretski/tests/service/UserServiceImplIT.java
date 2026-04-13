package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.PermissionRepo;
import com.nikolaihoretski.tests.repo.RoleRepo;
import com.nikolaihoretski.tests.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

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
    private UserRepo userRepo;
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
        assertTrue(result.roles().contains("ROLE_USER"));
        assertTrue(result.permissions().contains("READ"));

    }

    @Test
    void create_shouldSaveUser_WhenRequestIsValid() {

        UserCreateRequestDto createRequestDto = new UserCreateRequestDto(
                "my_user",
                "my_user",
                "my_user",
                "my_user@my_user.by",
                Set.of("USER"),
                Set.of("WRITE")
        );

        UserResponseWithIdUsernameDto result = userService.create(createRequestDto);

        assertNotNull(result);
        assertEquals(createRequestDto.username(), result.username());

        assertTrue(userRepo.existsByUsername(createRequestDto.username()));
    }

    @Test
    void create_shouldThrowException_WhenUserAlreadyExists() {

        User existingUser = new User();
        existingUser.setUsername("admin123");
        existingUser.setPassword("admin123");
        existingUser.setEmail("admin123@admin.by");
        existingUser.setName("admin");

        userRepo.save(existingUser);

        UserCreateRequestDto createRequestDto = new UserCreateRequestDto(
                "admin123",
                "admin",
                "admin123",
                null,
                Set.of("ADMIN"),
                Set.of("WRITE")
        );

        assertThrows(UserAlreadyExistException.class, () -> userService.create(createRequestDto));
    }

}
