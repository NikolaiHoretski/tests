package com.nikolaihoretski.tests.service.client;

import com.nikolaihoretski.tests.dto.UserResponseWithIdUsernameDto;
import com.nikolaihoretski.tests.dto.UserUpdateRequestDto;
import com.nikolaihoretski.tests.exception.UserNotAuthenticationException;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
import com.nikolaihoretski.tests.service.secutity.CustomUserDetails;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
public class ClientCrudServiceImpl implements ClientCrudService {

    private final JpaUserRepo jpaUserRepo;

    private static final String DEFAULT_UPDATE_LOG_CONSTRAINT =
            "user field <{}> in user with id <{}> and username <{}> was updated to field <{}>";

    public ClientCrudServiceImpl(JpaUserRepo jpaUserRepo) {
        this.jpaUserRepo = jpaUserRepo;
    }

    @Override
    @Transactional
    public @NonNull UserResponseWithIdUsernameDto update(@NonNull UserUpdateRequestDto updateRequestDto) {

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

        final User updatedUser = jpaUserRepo.save(user);

        return new UserResponseWithIdUsernameDto(
                updatedUser.getId(),
                updatedUser.getUsername());
    }

    @Override
    public void delete() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        final Object principle = authentication.getPrincipal();

        if(!(principle instanceof CustomUserDetails details)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        final Long id = details.getId();

        jpaUserRepo.deleteById(id);

        log.info("user with id {}, was deleted", id);
    }

}
