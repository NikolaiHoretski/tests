package com.nikolaihoretski.tests.service.jwt;

import com.nikolaihoretski.tests.dto.LoginDto;
import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtGeneratedFactoryService {

    private final SecretKey secretKey;

    public JwtGeneratedFactoryService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public @Nullable String createAccessToken(@NonNull LoginDto loginDto) {

        final String accessToken = Jwts.builder()
                .subject(loginDto.username())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(secretKey)
                .compact();

        if (accessToken != null) {
            log.info("Access token was generated");
        }

        return accessToken;
    }

    public @Nullable String createRefreshToken(@NotNull LoginDto loginDto) {

        final String refreshToken = Jwts.builder()
                .subject(loginDto.username())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 3))
                .signWith(secretKey)
                .compact();

        if (refreshToken != null) {
            log.info("Refresh token was generated");
        }

        return refreshToken;
    }

}
