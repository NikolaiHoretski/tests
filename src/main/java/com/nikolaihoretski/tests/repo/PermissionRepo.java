package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.Permission;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepo extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(@NonNull String name);
}
