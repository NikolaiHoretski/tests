package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.LoginDto;
import com.nikolaihoretski.tests.dto.TokenResponseDto;
import com.nikolaihoretski.tests.service.secutity.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDto) {

        final TokenResponseDto responseDto = authenticationService.verify(loginDto);

        return ResponseEntity.ok(responseDto);
    }

}
