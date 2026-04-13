package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.mapper.MapperUtils;
import com.nikolaihoretski.tests.model.Permission;
import com.nikolaihoretski.tests.model.Role;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.PermissionRepo;
import com.nikolaihoretski.tests.repo.RoleRepo;
import com.nikolaihoretski.tests.repo.UserRepo;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

        return userRepo.findByUsername(username)
                .map(MapperUtils::toFindUser)
                .orElseThrow(() -> new RuntimeException("user" + username + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findById(@NonNull Long id) {
        return userRepo.findById(id)
                .map(MapperUtils::toFindUser)
                .orElseThrow(() -> new RuntimeException("user" + id + " not found"));
    }

    @Override
    @Transactional
    public boolean create(@NonNull UserCreateRequestDto dto) {

        if (userRepo.existsByUsername(dto.username())) {
            throw new UserAlreadyExistException(dto.username());
        }

        final Set<Role> currentRole = (dto.roles() != null) ? roleRepo.findAllByNameIn(dto.roles()) : Set.of();
        final Set<Permission> currentPermissions = dto.permissions() != null ?
                permissionRepo.findAllByNameIn(dto.permissions()) : Set.of();

        User user = MapperUtils.toUser(dto);

        currentRole.forEach(user::addRole);
        currentPermissions.forEach(user::addPermission);

        userRepo.save(user);

        return true;
    }

}
