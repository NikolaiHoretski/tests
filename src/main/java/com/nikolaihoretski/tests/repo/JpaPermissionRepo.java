package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.Permission;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface JpaPermissionRepo extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByName(@NonNull String name);

    Set<Permission> findAllByNameIn(@NonNull Set<String> names);
}
