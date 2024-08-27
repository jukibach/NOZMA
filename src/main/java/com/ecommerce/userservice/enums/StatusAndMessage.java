package com.ecommerce.userservice.enums;

import com.ecommerce.userservice.constant.MessageId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusAndMessage implements IEnumStatusAndMessage {
    
    ACCOUNT_DOES_NOT_EXIST(MessageId.E101),
    
    ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS(MessageId.E102),
    
    ACCOUNT_HAS_BEEN_DELETED(MessageId.E103),
    
    INCORRECT_PASSWORD(MessageId.E104),
    
    ACCOUNT_ALREADY_EXISTED(MessageId.E105),
    
    PASSWORD_EXPIRED(MessageId.E106),
    
    TOKEN_EXPIRED(MessageId.E107),
    
    USER_DOES_NOT_EXIST(MessageId.E108);
    
    private final String message;
}
