package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.mapper.AccountMapper;
import com.nikolaihoretski.tests.model.Permission;
import com.nikolaihoretski.tests.model.Role;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.PermissionRepo;
import com.nikolaihoretski.tests.repo.RoleRepo;
import com.nikolaihoretski.tests.repo.UserRepo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PermissionRepo permissionRepo;

    private final AccountMapper accountMapper;

    private static final String DEFAULT_UPDATE_LOG_CONSTRAINT =
            "user field <{}> in user with id <{}> and username <{}> was updated to field <{}>";

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PermissionRepo permissionRepo, AccountMapper accountMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findByUsername(@NonNull String username) {

        final UserResponseDto responseDto = userRepo.findByUsername(username)
                .map(accountMapper::toUserResponseDto)
                .orElseThrow(() -> new RuntimeException("user" + username + " not found"));

        log.info("check user on findByUserName method. User with username <{}> was found", username);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findById(@NonNull Long id) {

        final UserResponseDto responseDto = userRepo.findById(id)
                .map(accountMapper::toUserResponseDto)
                .orElseThrow(() -> new RuntimeException("user" + id + " not found"));

        log.info("Check user on findById method. User with id <{}> was found", id);

        return responseDto;
    }

    @Override
    @Transactional
    public @NonNull UserResponseWithIdUsernameDto createForAdmin(@NonNull UserCreateRequestDto createRequestDto) {

        if (userRepo.existsByUsername(createRequestDto.username())) {
            throw new UserAlreadyExistException(createRequestDto.username());
        }

        log.info("Check exists of user in createForAdmin method. User with username <{}> exists", createRequestDto.username());

        final Set<Role> currentRole = (createRequestDto.roles() != null) ? roleRepo.findAllByNameIn(createRequestDto.roles()) : Set.of();
        final Set<Permission> currentPermissions = createRequestDto.permissions() != null ?
                permissionRepo.findAllByNameIn(createRequestDto.permissions()) : Set.of();

        final User user = accountMapper.toUser(createRequestDto);

        currentRole.forEach(user::addRole);
        currentPermissions.forEach(user::addPermission);

        userRepo.save(user);

        log.info("User with username <{}>, and id <{}> was saved", user.getUsername(), user.getId());

        return new UserResponseWithIdUsernameDto(
                user.getId(),
                user.getUsername()
        );
    }

    //only for user/client, not for admin
    @Override
    @Transactional
    public @NonNull UserResponseWithIdUsernameDto update(@NonNull UserUpdateRequestDto updateRequestDto) {

        final User user = userRepo.findById(updateRequestDto.id())
                .orElseThrow(() -> new UserNotFoundException(updateRequestDto.id()));

        final String password = user.getPassword();
        final String name = user.getName();
        final String email = user.getEmail();

        log.info("user with id <{}> and username <{}> was found", user.getId(), user.getUsername());

        if (!Objects.isNull(updateRequestDto.password()) &&
                !Objects.equals(updateRequestDto.password(), user.getPassword())) {
            user.setPassword(updateRequestDto.password());
            log.info(
                    DEFAULT_UPDATE_LOG_CONSTRAINT,
                    password, user.getId(), user.getUsername(), user.getPassword());
        }
        if (!Objects.isNull(updateRequestDto.name()) && !Objects.equals(updateRequestDto.name(), user.getName())) {
            user.setName(updateRequestDto.name());
            log.info(
                    DEFAULT_UPDATE_LOG_CONSTRAINT,
                    name, user.getId(), user.getUsername(), user.getName());
        }
        if (!Objects.isNull(updateRequestDto.email()) && !Objects.equals(updateRequestDto.email(), user.getEmail())) {
            user.setEmail(updateRequestDto.email());
            log.info(
                    DEFAULT_UPDATE_LOG_CONSTRAINT,
                    email, user.getId(), user.getUsername(), user.getEmail());
        }

        final User updatedUser = userRepo.save(user);

        return new UserResponseWithIdUsernameDto(
                updatedUser.getId(),
                updatedUser.getUsername());
    }

}
