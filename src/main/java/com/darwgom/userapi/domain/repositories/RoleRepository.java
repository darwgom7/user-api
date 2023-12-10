package com.darwgom.userapi.domain.repositories;

import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}

