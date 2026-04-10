package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserDto;
import com.nikolaihoretski.tests.model.UserRole;
import com.nikolaihoretski.tests.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private static final String DEFAULT_PREFIX = "ROLE_";

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {

        return userRepo.findByUsername(username)
                .map(user -> {
                    Set<String> roles = user.getUserRoles().stream()
                            .map(userRole -> DEFAULT_PREFIX + userRole.getRole().getName())
                            .collect(Collectors.toSet());

                    Set<String> permissions = user.getUserRoles().stream()
                            .map(UserRole::getRole)
                            .flatMap(role -> role.getRolePermissions().stream())
                            .map(p -> p.getPermission().getName())
                            .collect(Collectors.toSet());

                    return new UserDto(
                            user.getId(),
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

}
