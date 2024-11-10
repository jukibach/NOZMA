package com.nozma.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public sealed class BaseException
        extends RuntimeException
        permits AccountNotFoundException, BusinessException {
    
    private final HttpStatus status;
    private final String message;
    
    public BaseException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public BaseException(Throwable cause, HttpStatus status, String message) {
        super(cause);
        this.status = status;
        this.message = message;
    }
    
    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                         HttpStatus status, String message1) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
        this.message = message1;
    }
}
