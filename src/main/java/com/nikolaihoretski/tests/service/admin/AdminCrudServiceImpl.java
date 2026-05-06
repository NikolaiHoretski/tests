package com.nikolaihoretski.tests.service.admin;

import com.github.f4b6a3.uuid.UuidCreator;
import com.nikolaihoretski.tests.dto.DeleteUserResponseDto;
import com.nikolaihoretski.tests.dto.UserCreateForAdminRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.exception.ProtectedUserDeletedException;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.mapper.AccountMapper;
import com.nikolaihoretski.tests.model.Permission;
import com.nikolaihoretski.tests.model.Role;
import com.nikolaihoretski.tests.model.RoleAccess;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.JpaPermissionRepo;
import com.nikolaihoretski.tests.repo.JpaRoleRepo;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class AdminCrudServiceImpl implements AdminCrudService {

    private final JpaUserRepo jpaUserRepo;
    private final JpaRoleRepo jpaRoleRepo;
    private final JpaPermissionRepo jpaPermissionRepo;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminCrudServiceImpl(JpaUserRepo jpaUserRepo, JpaRoleRepo jpaRoleRepo, JpaPermissionRepo jpaPermissionRepo, AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
        this.jpaUserRepo = jpaUserRepo;
        this.jpaRoleRepo = jpaRoleRepo;
        this.jpaPermissionRepo = jpaPermissionRepo;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public @Nullable List<UserResponseDto> getAll() {

        final List<User> users = jpaUserRepo.findAllByIsEnabledAndIsDeletedFalse(true, false);

        log.info("find all users: {}", users);

        return accountMapper.toUserResponseDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public @Nullable List<DeleteUserResponseDto> getAllDeleteUsers() {

        final List<User> users = jpaUserRepo.findAllByIsDeleted(true);

        log.info("find all deleted users: {}", users);

        return accountMapper.toDeleteUserResponseDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public @Nullable List<UserResponseDto> getAllDisableUsers() {

        final List<User> users = jpaUserRepo.findAllByIsEnabled(false);

        log.info("find all disabled users: {}", users);

        return accountMapper.toUserResponseDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto getByUsername(@NonNull String username) {

        final UserResponseDto responseDto = jpaUserRepo.findByUsername(username)
                .filter(user -> !user.isDeleted())
                .map(accountMapper::toUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException(username));

        log.info("check user on findByUserName method. User with username <{}> was found", username);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto getById(@NonNull UUID id) {

        final UserResponseDto responseDto = jpaUserRepo.findById(id)
                .filter(user -> !user.isDeleted())
                .map(accountMapper::toUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException(id));

        log.info("Check user on findById method. User with id <{}> was found", id);

        return responseDto;
    }

    @Override
    @Transactional
    public boolean createForAdmin(@NonNull UserCreateForAdminRequestDto createRequestDto) {

        if (jpaUserRepo.existsByUsername(createRequestDto.username())) {
            log.info("Check exists of user in createForAdmin method. User with username <{}> exists",
                    createRequestDto.username());
            throw new UserAlreadyExistException(createRequestDto.username());
        }

        final User user = accountMapper.toUser(createRequestDto);
        final UUID uuid = UuidCreator.getTimeOrderedEpoch();
        user.setId(uuid);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setDeleted(false);
        final Set<Role> currentRole = (createRequestDto.roles() != null) ?
                jpaRoleRepo.findAllByNameIn(createRequestDto.roles()) :
                Set.of();
        final Set<Permission> currentPermissions = createRequestDto.permissions() != null ?
                jpaPermissionRepo.findAllByNameIn(createRequestDto.permissions()) :
                Set.of();
        currentRole.forEach(user::addRole);
        currentPermissions.forEach(user::addPermission);

       jpaUserRepo.save(user);

        log.info("User with username <{}>, and id <{}> was saved", user.getUsername(), user.getId());

        return true;
    }

    @Override
    @Transactional
    public void delete(@NonNull UUID id) {

        final List<UUID> uuids = jpaUserRepo.findAllByRole(RoleAccess.ADMIN.name());

        for(UUID uuid : uuids) {
            if (id.equals(uuid)) {
                throw new ProtectedUserDeletedException(id);
            }
        }

        if (!jpaUserRepo.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        jpaUserRepo.deleteById(id);

        log.info("User with id {} was deleted", id);
    }

}
