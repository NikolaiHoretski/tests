package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepo extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {
            "userRoles.role",
            "userPermissions.permission"
    })
    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    @EntityGraph(attributePaths = {
            "userRoles.role",
            "userPermissions.permission"
    })
    @NonNull
    Optional<User> findById(@NonNull Long id);


    boolean existsByUsername(@NonNull String username);

}
