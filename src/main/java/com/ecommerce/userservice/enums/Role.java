package com.ecommerce.userservice.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", 0),
    USER("ROLE_USER", 1),
    SELLER("ROLE_SELLER", 2),
    CUSTOMER_SUPPORT("ROLE_CUSTOMER_SUPPORT", 3);
    
    private final String roleName;
    private final Integer id;
    
    Role(String roleName, Integer id) {
        this.roleName = roleName;
        this.id = id;
    }
    
}
