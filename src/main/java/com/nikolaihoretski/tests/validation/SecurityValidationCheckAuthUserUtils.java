package com.nikolaihoretski.tests.validation;

import com.nikolaihoretski.tests.exception.UserIsNotAuthenticationException;
import com.nikolaihoretski.tests.service.secutity.CustomUserDetails;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class SecurityValidationCheckAuthUserUtils {

    private SecurityValidationCheckAuthUserUtils() {
        throw new UnsupportedOperationException(
                "This class " + SecurityValidationCheckAuthUserUtils.class.getName() + " is utility");
    }

    public static Authentication currentUserCheckIsValidAuth() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserIsNotAuthenticationException();
        }

        return authentication;
    }

    public static CustomUserDetails getCurrentUserId(@NotNull Authentication authentication) {

        final Object principle = authentication.getPrincipal();

        if (!(principle instanceof CustomUserDetails details)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return details;
    }

}
