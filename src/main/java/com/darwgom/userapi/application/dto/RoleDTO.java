package com.darwgom.userapi.application.dto;

import com.darwgom.userapi.domain.enums.RoleNameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;
    private RoleNameEnum name;
    public RoleDTO(RoleNameEnum name) {
        this.name = name;
    }
}
