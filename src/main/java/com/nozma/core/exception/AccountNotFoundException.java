package com.nozma.core.exception;

import com.nozma.core.enums.IEnumStatusAndMessage;
import com.nozma.core.enums.StatusAndMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountNotFoundException extends BaseException {
    private final transient Object result;
    
    public AccountNotFoundException(String message, Object result) {
        super(HttpStatus.NOT_FOUND, message);
        this.result = result;
    }
    
    public AccountNotFoundException() {
        super(HttpStatus.NOT_FOUND, StatusAndMessage.ACCOUNT_DOES_NOT_EXIST.getMessage());
        this.result = null;
    }
    
    public AccountNotFoundException(IEnumStatusAndMessage statusAndMessage) {
        super(null, statusAndMessage.getMessage());
        this.result = null;
    }
}
