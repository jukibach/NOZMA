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
    
    //    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^[0-9a-fA-F]{1,4}(?::[0-9a-fA-F]{1,4}){7}$",
//            message = "Invalid IP address format")
    private String ipAddress; // Supports both IPv4 and IPv6 addresses
    
    //    @NotBlank(message = "Login timestamp is required")
    private String loginTimestamp;
    
    private String deviceType;
    private String deviceOS;
    private String browserName;
    private String browserVersion;
    private String failureReason;
}
