package com.nikolaihoretski.tests.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Slf4j
@Service
public class JwtParserService {

    private final SecretKey secretKey;

    public JwtParserService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @NonNull
    public String extractUsernameFromToken(@NonNull String token) throws BadCredentialsException {

        if (token.isBlank()) {
            throw new BadCredentialsException("Jwt token is missing");
        }

        final Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

}
