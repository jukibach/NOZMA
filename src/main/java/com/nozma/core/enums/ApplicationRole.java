package com.nozma.core.enums;

import lombok.Getter;

@Getter
public enum ApplicationRole {
    ADMIN("admin", 0),
    NORMAL_USER("normalUser", 1),
    GUEST("guest", 2);
    
    private final String roleName;
    private final Integer id;
    
    ApplicationRole(String roleName, Integer id) {
        this.roleName = roleName;
        this.id = id;
    }
    
}
