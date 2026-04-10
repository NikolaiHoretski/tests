package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {
            "userRoles.role",
            "userPermissions.permission"
    })
    Optional<User> findByUsername(@NonNull String username);

    boolean existsByUsername(@NonNull String username);

}
