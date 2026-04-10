package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserDto;
import com.nikolaihoretski.tests.model.Permission;
import com.nikolaihoretski.tests.model.Role;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.PermissionRepo;
import com.nikolaihoretski.tests.repo.RoleRepo;
import com.nikolaihoretski.tests.repo.UserRepo;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PermissionRepo permissionRepo;

    private static final String DEFAULT_PREFIX = "ROLE_";

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PermissionRepo permissionRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(@NonNull String username) {

        return userRepo.findByUsername(username)
                .map(user -> {
                    Set<String> roles = user.getUserRoles().stream()
                            .map(userRole -> DEFAULT_PREFIX + userRole.getRole().getName())
                            .collect(Collectors.toSet());

                    Set<String> permissions = user.getUserPermissions().stream()
                            .map(p -> p.getPermission().getName())
                            .collect(Collectors.toSet());

                    return new UserDto(
                            user.getUsername(),
                            user.getPassword(),
                            user.getName(),
                            user.getEmail(),
                            user.isEnabled(),
                            roles,
                            permissions
                    );
                })
                .orElseThrow(() -> new RuntimeException("user" + username + " not found"));
    }

    @Override
    @Transactional
    public boolean create(@NonNull UserDto dto) {

        if (userRepo.existsByUsername(dto.username())) {
            throw new RuntimeException("user already exists");
        }

        final Set<Role> currentRole = (dto.roles() != null) ? roleRepo.findAllByNameIn(dto.roles()) : Set.of();
        final Set<Permission> currentPermissions = dto.permissions() != null ?
                permissionRepo.findAllByNameIn(dto.permissions()) : Set.of();

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setEnabled(true);

        currentRole.forEach(user::addRole);
        currentPermissions.forEach(user::addPermission);

        userRepo.save(user);

        return true;
    }

}
