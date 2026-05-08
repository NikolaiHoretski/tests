package com.nikolaihoretski.tests.service.jwt;

import com.nikolaihoretski.tests.dto.LoginDto;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtGeneratedFactoryService {

    private final SecretKey secretKey;

    public JwtGeneratedFactoryService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }



    public @Nullable String createToken(@NonNull UUID uuid, long expirationMillis) {

        return Jwts.builder()
                .subject(uuid.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey)
                .compact();
    }

    public @Nullable String createAccessToken(@NonNull UUID uuid) {
        return createToken(uuid, 1000L * 10);
    }

    public @Nullable String createRefreshToken(@NonNull UUID uuid) {
        return createToken(uuid, 1000L * 60 * 60 * 24 * 90);
    }



}
