package com.nikolaihoretski.tests.service.secutity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID id;
    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UUID id,
                             String username,
                             String password,
                             boolean isEnabled,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

}
