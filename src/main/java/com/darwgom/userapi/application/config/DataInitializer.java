package com.darwgom.userapi.application.config;

import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.enums.RoleNameEnum;
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
        for (RoleNameEnum roleName : RoleNameEnum.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }
}

