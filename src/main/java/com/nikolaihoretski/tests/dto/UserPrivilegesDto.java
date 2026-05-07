package com.nikolaihoretski.tests.dto;

import java.util.Set;
import java.util.UUID;

public record UserPrivilegesDto(
        UUID uuid,
        Set<String> privileges
) {
}
