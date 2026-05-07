package com.nikolaihoretski.tests.filter.jwt;

import com.nikolaihoretski.tests.service.jwt.JwtParserService;
import com.nikolaihoretski.tests.service.secutity.JpaUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
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

        final String path = request.getServletPath();
        if(path.equals("/api/login") || path.equals("/api/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            final UUID uuid = extractUsername(token);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(uuid, request);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expired\"}");
        } catch (Exception e) {
            log.error("Auth error: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private UUID extractUsername(@NonNull String token) {
            return UUID.fromString(parserService.extractUuidFromToken(token));
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
