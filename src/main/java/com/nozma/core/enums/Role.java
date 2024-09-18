package com.nozma.core.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", 0),
    PAID_USER("ROLE_PAID_USER", 1),
    USER("ROLE_USER", 2);
    
    private final String roleName;
    private final Integer id;
    
    Role(String roleName, Integer id) {
        this.roleName = roleName;
        this.id = id;
    }
    
}
