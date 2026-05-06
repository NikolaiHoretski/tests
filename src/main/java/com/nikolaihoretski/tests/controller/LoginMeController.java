package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.*;
import com.nikolaihoretski.tests.service.secutity.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginMeController {

    private final AuthenticationService authenticationService;

    public LoginMeController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody LoginDto loginDto,
                                                  HttpServletResponse response) {

        final AuthResult result = authenticationService.verify(loginDto);
        final Cookie cookie = new Cookie("refreshToken", result.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserLoginResponseDto(result.id(), result.username(), result.accessToken())
        );
    }

}
