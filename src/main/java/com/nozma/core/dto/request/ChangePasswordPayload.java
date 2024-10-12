package com.nozma.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordPayload {
    
    private String oldPassword;
    
    private String newPassword;
    
    private String confirmPassword;
    
    
}
