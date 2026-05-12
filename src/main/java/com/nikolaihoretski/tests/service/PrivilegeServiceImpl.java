package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserPrivilegesDto;
import com.nikolaihoretski.tests.validation.SecurityValidationCheckAuthUserUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Override
    public @NonNull UserPrivilegesDto getUserPrivileges() {

        final Authentication authentication = SecurityValidationCheckAuthUserUtils.currentUserCheckIsValidAuth();

        final UUID uuid = SecurityValidationCheckAuthUserUtils.getCurrentUserId(authentication).getId();

        log.info("Getting user privileges from database for uuid: {}", uuid);

        final Set<String> listOfPrivileges =
                SecurityValidationCheckAuthUserUtils.currentUserCheckIsValidAuth().getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

        log.info("get privilegesList: {}, by user uuid: {}", listOfPrivileges, uuid);

        return new UserPrivilegesDto(uuid, listOfPrivileges);
    }

}
