package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
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
class ClientCrudServiceImplIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private AdminCrudService adminCrudService;
    @Autowired
    private ClientCrudService clientCrudService;
    @Autowired
    private JpaUserRepo jpaUserRepo;

    @Test
    void shouldReturnFindUserDtoByUsernameWhenUserExists() {

        final String username = "admin";

        final UserResponseDto actualResult = adminCrudService.findByUsername(username);

        assertNotNull(actualResult);
        assertEquals(username, actualResult.username());
        assertTrue(actualResult.roles().contains("ROLE_ADMIN"));
        assertTrue(actualResult.permissions().contains("READ"));
    }

    @Test
    void shouldReturnFindUserDtoByIdWhenUserExists() {

        final Long userId = 1L;

        final UserResponseDto actualResult = adminCrudService.findById(userId);

        assertNotNull(actualResult);
        assertEquals(userId, actualResult.id());
        assertTrue(actualResult.roles().contains("ROLE_USER"));
        assertTrue(actualResult.permissions().contains("READ"));

    }

    @Test
    void create_ForAdmin_shouldSaveUser_WhenRequestIsValid() {

        UserCreateRequestDto createRequestDto = new UserCreateRequestDto(
                "my_user",
                "my_user",
                "my_user",
                "my_user@my_user.by",
                Set.of("USER"),
                Set.of("WRITE")
        );

        UserResponseWithIdUsernameDto actualResult = adminCrudService.createForAdmin(createRequestDto);

        assertNotNull(actualResult);
        assertEquals(createRequestDto.username(), actualResult.username());

        assertTrue(jpaUserRepo.existsByUsername(createRequestDto.username()));
    }

    @Test
    void create_ForAdmin_shouldThrowException_WhenUserAlreadyExists() {

        User existingUser = new User();
        existingUser.setUsername("admin123");
        existingUser.setPassword("admin123");
        existingUser.setEmail("admin123@admin.by");
        existingUser.setName("admin");

        jpaUserRepo.save(existingUser);

        UserCreateRequestDto actualResult = new UserCreateRequestDto(
                "admin123",
                "admin",
                "admin123",
                null,
                Set.of("ADMIN"),
                Set.of("WRITE")
        );

        assertThrows(UserAlreadyExistException.class, () -> adminCrudService.createForAdmin(actualResult));
    }

    @Test
    void update_shouldUpdateUser_WhenRequestIsValid() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("me");
        existingUser.setPassword("me");
        existingUser.setEmail("me@me.by");
        existingUser.setName("me");

        jpaUserRepo.save(existingUser);

        UserUpdateRequestDto createRequestDto = new UserUpdateRequestDto(
                1L,
                "me",
                "not_me",
                null
        );

        UserResponseWithIdUsernameDto actualResult = clientCrudService.update(createRequestDto);

        assertNotNull(actualResult);
    }

}
