package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface JpaRoleRepo extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(@NonNull String name);

    Set<Role> findAllByNameIn(@NonNull Set<String> names);
}
