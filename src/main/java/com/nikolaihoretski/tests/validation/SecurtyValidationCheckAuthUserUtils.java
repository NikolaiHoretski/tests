package com.nikolaihoretski.tests.validation;

import com.nikolaihoretski.tests.exception.UserIsNotAuthenticationException;
import com.nikolaihoretski.tests.service.secutity.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class SecurtyValidationCheckAuthUserUtils {

    private SecurtyValidationCheckAuthUserUtils() {
        throw new UnsupportedOperationException("This class " + getClass().getName() + " is utility");
    }

    public static UUID currentUserCheckIsValidAndReturnId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserIsNotAuthenticationException();
        }

        final Object principle = authentication.getPrincipal();

        if (!(principle instanceof CustomUserDetails details)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return details.getId();
    }

}
