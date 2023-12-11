package com.darwgom.userapi.domain.enums;

public enum RoleNameEnum {
    ROLE_USER("role_user"),
    ROLE_ADMIN("role_admin");

    private final String value;

    RoleNameEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

