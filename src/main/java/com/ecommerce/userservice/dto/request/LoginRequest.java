package com.ecommerce.userservice.dto.request;


import com.ecommerce.userservice.annotations.CustomNotNull;
import com.ecommerce.userservice.constant.FieldNamesConstant;
import com.ecommerce.userservice.constant.MessageConstant;
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
