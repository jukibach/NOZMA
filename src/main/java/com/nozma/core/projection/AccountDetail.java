package com.nozma.core.projection;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface AccountDetail extends Serializable {
    long getId();
    String getAccountName();
    String getEmail();
    
    UserInfo getUser();
    
    LocalDateTime getCreatedDate();
    LocalDateTime getUpdatedDate();
    
    String getStatus();
    boolean getIsLocked();
    
    interface UserInfo {
        String getFirstName();
        String getLastName();
        String getBirthdate();
        
        default String getFullName() {
            return getFirstName().concat(" ").concat(getLastName());
        }
    }
    
}
