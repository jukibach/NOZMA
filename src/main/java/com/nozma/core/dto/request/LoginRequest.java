package com.nozma.core.dto.request;


import com.nozma.core.annotations.CustomNotNull;
import com.nozma.core.constant.FieldNamesConstant;
import com.nozma.core.constant.MessageConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.ACCOUNT_NAME)
    private String accountName;
    
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.PASSWORD)
    private String password;
    
}
