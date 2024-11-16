package com.mdbackend.mdbackend.enums;

public enum RoleEnum {
    PASSENGER("ROLE_PASSENGER"),
    SUPER_ADMIN("ROLE_SUPERADMIN"),
    BUS_ADMIN("ROLE_BUSADMIN"),
    BUS("ROLE_BUS");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
