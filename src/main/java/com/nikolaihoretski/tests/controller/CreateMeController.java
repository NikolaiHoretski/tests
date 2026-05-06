package com.nikolaihoretski.tests.controller;

import com.nikolaihoretski.tests.dto.AuthResult;
import com.nikolaihoretski.tests.dto.UserCreateRequestDto;
import com.nikolaihoretski.tests.dto.UserCreateResponseDto;
import com.nikolaihoretski.tests.service.client.ClientCrudService;
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
public class CreateMeController {

    private final ClientCrudService clientCrudService;

    public CreateMeController(ClientCrudService clientCrudService) {
        this.clientCrudService = clientCrudService;
    }

    @PostMapping("/register/me")
    public ResponseEntity<UserCreateResponseDto> register(@RequestBody UserCreateRequestDto requestDto,
                                                          HttpServletResponse response) {
        final AuthResult result = clientCrudService.create(requestDto);
        final Cookie cookie = new Cookie("refreshToken", result.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/regresh");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserCreateResponseDto(result.id(), result.username(), result.accessToken())
        );
    }

}
