package com.darwgom.userapi.application.config;

import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.enums.RoleName;
import com.darwgom.userapi.domain.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }
}

