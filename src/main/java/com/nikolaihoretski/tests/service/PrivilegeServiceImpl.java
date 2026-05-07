package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserPrivilegesDto;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
import com.nikolaihoretski.tests.validation.SecurtyValidationCheckAuthUserUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeService{

    private final JpaUserRepo jpaUserRepo;

    public PrivilegeServiceImpl(JpaUserRepo jpaUserRepo) {
        this.jpaUserRepo = jpaUserRepo;
    }

    @Override
    public @NonNull UserPrivilegesDto getUserPrivileges() {

        final UUID uuid = SecurtyValidationCheckAuthUserUtils.currentUserCheckIsValidAndReturnId();

        if(!jpaUserRepo.existsById(uuid)) {
            throw new UserNotFoundException(uuid);
        }

        final Set<String> listOfPrivileges = jpaUserRepo.findPrivilegesByUserId(uuid);

        log.info("get privilegesList: {}, by user uuid: {}", listOfPrivileges, uuid);

        return new UserPrivilegesDto(uuid, listOfPrivileges);
    }

}
