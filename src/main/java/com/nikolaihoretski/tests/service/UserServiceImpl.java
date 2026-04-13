package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.mapper.MapperUtils;
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

import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PermissionRepo permissionRepo;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PermissionRepo permissionRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findByUsername(@NonNull String username) {

        final UserResponseDto responseDto = userRepo.findByUsername(username)
                .map(MapperUtils::toFindUser)
                .orElseThrow(() -> new RuntimeException("user" + username + " not found"));

        log.info("check user on findByUserName method. User with username {} was found", username);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findById(@NonNull Long id) {

        final UserResponseDto responseDto = userRepo.findById(id)
                .map(MapperUtils::toFindUser)
                .orElseThrow(() -> new RuntimeException("user" + id + " not found"));

        log.info("Check user on findById method. User with id {} was found", id);

        return responseDto;
    }

    @Override
    @Transactional
    public UserResponseWithIdUsernameDto create(@NonNull UserCreateRequestDto createRequestDto) {

        if (userRepo.existsByUsername(createRequestDto.username())) {
            throw new UserAlreadyExistException(createRequestDto.username());
        }

        log.info("Check exists of user in create method. User with username {} exists", createRequestDto.username());

        final Set<Role> currentRole = (createRequestDto.roles() != null) ? roleRepo.findAllByNameIn(createRequestDto.roles()) : Set.of();
        final Set<Permission> currentPermissions = createRequestDto.permissions() != null ?
                permissionRepo.findAllByNameIn(createRequestDto.permissions()) : Set.of();

        final User user = MapperUtils.toUser(createRequestDto);

        currentRole.forEach(user::addRole);
        currentPermissions.forEach(user::addPermission);

        userRepo.save(user);

        log.info("User with username {}, and id {} was saved", user.getUsername(), user.getId());

        return new UserResponseWithIdUsernameDto(
                user.getId(),
                user.getUsername()
        );
    }

}
