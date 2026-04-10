package com.nikolaihoretski.tests.repo;

import com.nikolaihoretski.tests.model.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(@NonNull String name);
}
