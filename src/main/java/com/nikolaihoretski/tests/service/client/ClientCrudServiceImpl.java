package com.nikolaihoretski.tests.service.client;

import com.github.f4b6a3.uuid.UuidCreator;
import com.nikolaihoretski.tests.dto.AuthResult;
import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.mapper.AccountMapper;
import com.nikolaihoretski.tests.model.*;
import com.nikolaihoretski.tests.repo.JpaPermissionRepo;
import com.nikolaihoretski.tests.repo.JpaRoleRepo;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
import com.nikolaihoretski.tests.service.jwt.JwtGeneratedFactoryService;
import com.nikolaihoretski.tests.validation.SecurityValidationCheckAuthUserUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ClientCrudServiceImpl implements ClientCrudService {

    private final JpaUserRepo jpaUserRepo;
    private final JpaRoleRepo jpaRoleRepo;
    private final JpaPermissionRepo jpaPermissionRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtGeneratedFactoryService jwtGeneratedFactoryService;

    private static final String DEFAULT_UPDATE_LOG_CONSTRAINT =
            "user field <{}> in user with id <{}> and username <{}> was updated to field <{}>";
    private final AccountMapper accountMapper;

    public ClientCrudServiceImpl(JpaUserRepo jpaUserRepo, JpaRoleRepo jpaRoleRepo, JpaPermissionRepo jpaPermissionRepo, PasswordEncoder passwordEncoder, JwtGeneratedFactoryService jwtGeneratedFactoryService, AccountMapper accountMapper) {
        this.jpaUserRepo = jpaUserRepo;
        this.jpaRoleRepo = jpaRoleRepo;
        this.jpaPermissionRepo = jpaPermissionRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtGeneratedFactoryService = jwtGeneratedFactoryService;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public @NonNull AuthResult create(@NonNull UserCreateRequestDto createRequestDto) {

        if (jpaUserRepo.existsByUsername(createRequestDto.username())) {
            log.info("Check exists of user in create method. User with username <{}> exists",
                    createRequestDto.username());
            throw new UserAlreadyExistException(createRequestDto.username());
        }

        final User user = accountMapper.toUser(createRequestDto);
        final UUID uuid = UuidCreator.getTimeOrderedEpoch();
        user.setId(uuid);
        user.setPassword(passwordEncoder.encode(createRequestDto.password()));
        user.setEnabled(true);
        user.setDeleted(false);
        user.setCreatedBy(uuid);

        final Set<Role> currentRole = jpaRoleRepo.findAllByNameIn(Set.of(RoleAccess.USER.name()));
        final Set<Permission> currentPermission = jpaPermissionRepo.findAllByNameIn(Set.of(PermissionAccess.READ.name()));

        currentRole.forEach(user::addRole);
        currentPermission.forEach(user::addPermission);

        jpaUserRepo.save(user);

        log.info("User with username <{}>, and id <{}> was saved", user.getUsername(), user.getId());

        final String createAccessToken = jwtGeneratedFactoryService.createAccessToken(uuid);
        final String createRefreshToken = jwtGeneratedFactoryService.createRefreshToken(uuid);

        return new AuthResult(
                user.getId(),
                user.getUsername(),
                createAccessToken,
                createRefreshToken
        );
    }

    @Override
    @Transactional
    public boolean update(@NonNull UserUpdateRequestDto updateRequestDto) {

        final User user = jpaUserRepo.findById(updateRequestDto.id())
                .orElseThrow(() -> new UserNotFoundException(updateRequestDto.id()));

        final String password = user.getPassword();
        final String name = user.getName();
        final String email = user.getEmail();

        log.info("user with id <{}> and username <{}> was found", user.getId(), user.getUsername());

        if (!Objects.isNull(updateRequestDto.password()) &&
                !Objects.equals(updateRequestDto.password(), user.getPassword())) {
            user.setPassword(updateRequestDto.password());
            log.info(
                    DEFAULT_UPDATE_LOG_CONSTRAINT,
                    password, user.getId(), user.getUsername(), user.getPassword());
        }
        if (!Objects.isNull(updateRequestDto.name()) && !Objects.equals(updateRequestDto.name(), user.getName())) {
            user.setName(updateRequestDto.name());
            log.info(
                    DEFAULT_UPDATE_LOG_CONSTRAINT,
                    name, user.getId(), user.getUsername(), user.getName());
        }
        if (!Objects.isNull(updateRequestDto.email()) && !Objects.equals(updateRequestDto.email(), user.getEmail())) {
            user.setEmail(updateRequestDto.email());
            log.info(
                    DEFAULT_UPDATE_LOG_CONSTRAINT,
                    email, user.getId(), user.getUsername(), user.getEmail());
        }

        jpaUserRepo.save(user);

        return true;
    }

    @Override
    @Transactional
    public void delete() {

        final Authentication authentication = SecurityValidationCheckAuthUserUtils.currentUserCheckIsValidAuth();

        final UUID id = SecurityValidationCheckAuthUserUtils.getCurrentUserId(authentication).getId();

        jpaUserRepo.deleteById(id);

        SecurityContextHolder.clearContext();

        log.info("user with id {}, was deleted", id);
    }

}
