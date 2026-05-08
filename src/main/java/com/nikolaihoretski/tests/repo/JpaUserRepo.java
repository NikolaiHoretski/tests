package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface JpaUserRepo extends JpaRepository<User, UUID> {

    boolean existsById(@NonNull UUID id);

    @NonNull
    List<User> findAllByIsEnabledAndIsDeletedFalse(boolean isEnabled, boolean isDeleted);

    @NonNull
    List<User> findAllByIsEnabled(boolean isEnabled);

    @NonNull
    List<User> findAllByIsDeleted(boolean isDeleted);

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
    Optional<User> findById(@NonNull UUID id);


    boolean existsByUsername(@NonNull String username);

    @NonNull
    @Query("select u.id from User u JOIN u.userRoles ur where ur.role.name = :role")
    List<UUID> findAllByRole(@NonNull String role);

    @Query("select count(u) > 0 from User u JOIN u.userRoles ur where u.id = :uuid and ur.role.name = :roleName")
    boolean existsByIdAndRole(@NonNull UUID uuid, @NonNull String roleName);

    @NonNull
    @Query("""
            select concat('ROLE_', ur.role.name) from User u JOIN u.userRoles ur where u.id = :uuid
            UNION
            select rp.permission.name from User u JOIN u.userPermissions rp where u.id = :uuid
            """)
    Set<String> findPrivilegesByUserId(@NonNull UUID uuid);

}
