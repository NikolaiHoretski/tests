package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(@NonNull String name);

    Set<Role> findAllByNameIn(@NonNull Set<String> names);
}
