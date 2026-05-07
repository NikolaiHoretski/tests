package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.*;
import com.nikolaihoretski.tests.service.secutity.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class AuthMeController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    public AuthMeController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthLoginResponseDto> login(@RequestBody LoginDto loginDto,
                                                          @NonNull HttpServletResponse response) {

        final AuthResult result = authenticationServiceImpl.verify(loginDto);
        setCookie(response, result.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserAuthLoginResponseDto(result.id(), result.username(), result.accessToken())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserAuthRefreshResponseDto> refreshSession(@CookieValue(name = "refreshToken") @NonNull String currentRefreshToken,
                                                                   @NonNull HttpServletResponse response) {

        final AuthResult result = authenticationServiceImpl.refresh(currentRefreshToken);
        setCookie(response, result.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserAuthRefreshResponseDto(result.id(), result.accessToken())
        );
    }

    private void setCookie(@NonNull HttpServletResponse response, @NonNull String token) {
        final ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
