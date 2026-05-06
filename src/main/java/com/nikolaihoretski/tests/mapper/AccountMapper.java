package com.nikolaihoretski.tests.mapper;

import com.nikolaihoretski.tests.dto.DeleteUserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateForAdminRequestDto;
import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.model.UserPermission;
import com.nikolaihoretski.tests.model.UserRole;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    private static final String DEFAULT_PREFIX = "ROLE_";

    public abstract User toUser(@Nullable UserCreateForAdminRequestDto dto);

    public abstract User toUser(@Nullable UserCreateRequestDto dto);

    @Mapping(target = "isEnabled", source = "enabled")
    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "mapRoles")
    @Mapping(target = "permissions", source = "userPermissions", qualifiedByName = "mapPermissions")
    public abstract UserResponseDto toUserResponseDto(@Nullable User user);

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

    @Mapping(target = "userInfo", source = "user")
    @Mapping(target = "isDeleted", source = "deleted")
    public abstract DeleteUserResponseDto toDeleteUserResponseDto(@Nullable User user);

    public abstract List<UserResponseDto> toUserResponseDtoList(@Nullable List<User> users);

    public abstract List<DeleteUserResponseDto> toDeleteUserResponseDtoList(@Nullable List<User> users);

}

