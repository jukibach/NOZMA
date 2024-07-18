package com.ecommerce.userservice.enums;

import lombok.Getter;

@Getter
public enum Privilege {
    GET_USERS("GET_USERS"),
    GET_USER_DETAIL("GET_USER_DETAIL");
    
    private final String privilegeName;
    
    Privilege(String privilegeName) {
        this.privilegeName = privilegeName;
    }
    
}
