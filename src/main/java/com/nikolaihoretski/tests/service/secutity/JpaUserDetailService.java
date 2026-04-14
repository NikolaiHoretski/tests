package com.nikolaihoretski.tests.service.secutity;

import com.nikolaihoretski.tests.repo.JpaUserRepo;
import lombok.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaUserDetailService implements UserDetailsService {

    private final JpaUserRepo jpaUserRepo;

    private static final String ROLE_PREFIX = "ROLE_";

    public JpaUserDetailService(JpaUserRepo jpaUserRepo) {
        this.jpaUserRepo = jpaUserRepo;
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        return jpaUserRepo.findByUsername(username)
                .map(user -> {
                   final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                   user.getUserRoles().forEach(role ->
                           authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getRole().getName())));

                   user.getUserPermissions().forEach(permission ->
                           authorities.add(new SimpleGrantedAuthority(permission.getPermission().getName())));

                    return new CustomUserDetails(
                            user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            user.isEnabled(),
                            authorities
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

}
