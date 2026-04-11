package com.nikolaihoretski.tests.mapper;

import com.nikolaihoretski.tests.dto.CreateUserDto;
import com.nikolaihoretski.tests.dto.FindUserDto;
import com.nikolaihoretski.tests.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class Mapper {

    private static final String DEFAULT_PREFIX = "ROLE_";

    public static User toUser(CreateUserDto dto) {

        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setEnabled(true);

        return user;
    }

    public static FindUserDto toFindUser(User user) {

        if (user == null) {
            return null;
        }

        final Set<String> roles = user.getUserRoles().stream()
                .map(userRole -> DEFAULT_PREFIX + userRole.getRole().getName())
                .collect(Collectors.toSet());

        final Set<String> permissions = user.getUserPermissions().stream()
                .map(p -> p.getPermission().getName())
                .collect(Collectors.toSet());

        return new FindUserDto(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.isEnabled(),
                roles,
                permissions
        );
    }

}
