package com.nikolaihoretski.tests.filter.jwt;

import com.nikolaihoretski.tests.service.jwt.JwtParserService;
import com.nikolaihoretski.tests.service.secutity.JpaUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtParserService parserService;
    private final JpaUserDetailService userDetailService;

    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public JwtFilter(JwtParserService parserService, JpaUserDetailService userDetailService) {
        this.parserService = parserService;
        this.userDetailService = userDetailService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final UUID uuid = extractUsername(token);

        if (uuid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticateUser(uuid, request);
        }

        filterChain.doFilter(request, response);
    }

    private UUID extractUsername(@NonNull String token) {

        try {
            return UUID.fromString(parserService.extractUuidFromToken(token));
        } catch (Exception e) {
            log.error("Failed to extract username from token ", e);
        }

        return null;
    }


    private void authenticateUser(@NonNull UUID uuid, @NonNull HttpServletRequest request) {

        try {
            final UserDetails userDetails = userDetailService.loadUserByUsername(uuid);
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (UsernameNotFoundException e) {
            log.warn("User with name: {} not found", uuid);
        }
    }

}
