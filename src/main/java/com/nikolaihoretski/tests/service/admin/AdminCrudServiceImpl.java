package com.nikolaihoretski.tests.service.admin;

import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserResponseDto;
import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.exception.UserAlreadyExistException;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.mapper.AccountMapper;
import com.nikolaihoretski.tests.model.Permission;
import com.nikolaihoretski.tests.model.Role;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.JpaPermissionRepo;
import com.nikolaihoretski.tests.repo.JpaRoleRepo;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AdminCrudServiceImpl implements AdminCrudService {

    private final JpaUserRepo jpaUserRepo;

    private final JpaRoleRepo jpaRoleRepo;

    private final JpaPermissionRepo jpaPermissionRepo;

    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    private static final Long PROTECTED_USER_ID = 1L;

    public AdminCrudServiceImpl(JpaUserRepo jpaUserRepo, JpaRoleRepo jpaRoleRepo, JpaPermissionRepo jpaPermissionRepo, AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
        this.jpaUserRepo = jpaUserRepo;
        this.jpaRoleRepo = jpaRoleRepo;
        this.jpaPermissionRepo = jpaPermissionRepo;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public @NonNull List<UserResponseDto> findAll() {

        final List<User> users = jpaUserRepo.findAll().stream()
                .filter(user -> !user.isDeleted())
                .toList();

        return accountMapper.toUserResponseDtos(users);
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findByUsername(@NonNull String username) {

        final UserResponseDto responseDto = jpaUserRepo.findByUsername(username)
                .filter(user -> !user.isDeleted())
                .map(accountMapper::toUserResponseDto)
                .orElseThrow(() -> new RuntimeException("user" + username + " not found"));

        log.info("check user on findByUserName method. User with username <{}> was found", username);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponseDto findById(@NonNull Long id) {

        final UserResponseDto responseDto = jpaUserRepo.findById(id)
                .filter(user -> !user.isDeleted())
                .map(accountMapper::toUserResponseDto)
                .orElseThrow(() -> new RuntimeException("user" + id + " not found"));

        log.info("Check user on findById method. User with id <{}> was found", id);

        return responseDto;
    }

    @Override
    @Transactional
    public @NonNull UserResponseWithIdUsernameDto createForAdmin(@NonNull UserCreateRequestDto createRequestDto) {

        if (jpaUserRepo.existsByUsername(createRequestDto.username())) {
            throw new UserAlreadyExistException(createRequestDto.username());
        }

        log.info("Check exists of user in createForAdmin method. User with username <{}> exists", createRequestDto.username());

        final Set<Role> currentRole = (createRequestDto.roles() != null) ? jpaRoleRepo.findAllByNameIn(createRequestDto.roles()) : Set.of();
        final Set<Permission> currentPermissions = createRequestDto.permissions() != null ?
                jpaPermissionRepo.findAllByNameIn(createRequestDto.permissions()) : Set.of();

        final User user = accountMapper.toUser(createRequestDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        currentRole.forEach(user::addRole);
        currentPermissions.forEach(user::addPermission);

        jpaUserRepo.save(user);

        log.info("User with username <{}>, and id <{}> was saved", user.getUsername(), user.getId());

        return new UserResponseWithIdUsernameDto(
                user.getId(),
                user.getUsername()
        );
    }

    @Override
    public void delete(@NonNull Long id) {

        if (id.equals(PROTECTED_USER_ID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this user must not be deleted");
        }

        if (!jpaUserRepo.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        jpaUserRepo.deleteById(id);

        log.info("User with id {} was deleted", id);
    }

}
