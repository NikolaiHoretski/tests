package com.nikolaihoretski.tests.mapper;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.model.UserPermission;
import com.nikolaihoretski.tests.model.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    private static final String DEFAULT_PREFIX = "ROLE_";

    public abstract User toUser(@NonNull UserCreateRequestDto dto);

    @Mapping(target = "isEnabled", source = "enabled")
    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "mapRoles")
    @Mapping(target = "permissions", source = "userPermissions", qualifiedByName = "mapPermissions")
    public abstract UserResponseDto toUserResponseDto(@NotNull User user);

    @Named("mapRoles")
    protected Set<String> mapRoles(@NonNull Set<UserRole> userRoles) {

        return userRoles.stream()
                .map(userRole -> DEFAULT_PREFIX + userRole.getRole().getName())
                .collect(Collectors.toSet());
    }

    @Named("mapPermissions")
    protected Set<String> mapPermissions(@NonNull Set<UserPermission> userPermissions) {

        return userPermissions.stream()
                .map(userPermission -> userPermission.getPermission().getName())
                .collect(Collectors.toSet());
    }

}

