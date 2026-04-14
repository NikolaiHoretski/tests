package com.nikolaihoretski.tests.service.secutity;

import com.nikolaihoretski.tests.repo.UserRepo;
import lombok.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    private static final String ROLE_PREFIX = "ROLE_";

    public JpaUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        return userRepo.findByUsername(username)
                .map(user -> {
                    Set<String> authorities = user.getUserRoles()
                            .stream()
                            .map(userRole -> ROLE_PREFIX + userRole.getRole().getName())
                            .collect(Collectors.toSet());

                    user.getUserPermissions().stream()
                            .map(userPermission -> userPermission.getPermission().getName())
                            .forEach(authorities::add);

                    return User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .disabled(!user.isEnabled())
                            .authorities(authorities.toArray(String[]::new))
                            .build();

                })
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

}
