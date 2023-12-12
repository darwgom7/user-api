package com.darwgom.userapi.domain.repositories;

import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleNameEnum roleName);
}

