package com.nikolaihoretski.tests.service.secutity;

import com.nikolaihoretski.tests.dto.AuthResult;
import com.nikolaihoretski.tests.dto.LoginDto;
import com.nikolaihoretski.tests.exception.UserNotFoundException;
import com.nikolaihoretski.tests.model.User;
import com.nikolaihoretski.tests.repo.JpaUserRepo;
import com.nikolaihoretski.tests.service.jwt.JwtGeneratedFactoryService;
import com.nikolaihoretski.tests.service.jwt.JwtParserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final JwtGeneratedFactoryService jwtGeneratedFactoryService;
    private final JpaUserRepo jpaUserRepo;
    private final JwtParserService parserService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtGeneratedFactoryService jwtGeneratedFactoryService, JpaUserRepo jpaUserRepo, JwtParserService parserService) {
        this.authenticationManager = authenticationManager;
        this.jwtGeneratedFactoryService = jwtGeneratedFactoryService;
        this.jpaUserRepo = jpaUserRepo;
        this.parserService = parserService;
    }

    @Override
    @NonNull
    public AuthResult verify(@NonNull LoginDto loginDto) {
        try {
            final Authentication authenticationCredentials = new UsernamePasswordAuthenticationToken(
                    loginDto.username(),
                    loginDto.password()
            );
            authenticationManager.authenticate(authenticationCredentials);
        } catch (AuthenticationException e) {
            log.error("Error to authenticate for username: {}", loginDto.username());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final User user = jpaUserRepo.findByUsername(loginDto.username())
                .orElseThrow(() -> new UserNotFoundException(loginDto.username()));

        final UUID uuid = user.getId();

        final String accessToken = jwtGeneratedFactoryService.createAccessToken(uuid);
        final String refreshToken = jwtGeneratedFactoryService.createRefreshToken(uuid);

        return new AuthResult(uuid, user.getUsername(),accessToken, refreshToken);
    }

    @Override
    public @NonNull AuthResult refresh(@NonNull String refreshToken) {

        final UUID uuid = UUID.fromString(parserService.extractUuidFromToken(refreshToken));

        final User user = jpaUserRepo.findById(uuid).orElseThrow(() -> new UserNotFoundException(uuid));

        final String newAccessToken = jwtGeneratedFactoryService.createAccessToken(uuid);
        final String newRefreshToken = jwtGeneratedFactoryService.createRefreshToken(uuid);

        return new AuthResult(uuid, user.getUsername(), newAccessToken, newRefreshToken);
    }

}
