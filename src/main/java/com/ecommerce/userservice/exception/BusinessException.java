package com.ecommerce.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends BaseException {
    private final transient Object result;
    
    public BusinessException(HttpStatus status, String message, Object result) {
        super(status, message);
        this.result = result;
    }
    
    
    public BusinessException(String message) {
        super(null, message);
        this.result = null;
    }
}
