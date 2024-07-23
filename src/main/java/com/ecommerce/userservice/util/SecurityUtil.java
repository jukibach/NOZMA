package com.ecommerce.userservice.util;

import com.ecommerce.userservice.entity.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}
    
    public static String getCurrentAccountName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String accountName = null;
        
        if (CommonUtil.isNonNullOrNonEmpty(authentication)) {
            if (authentication.getPrincipal() instanceof Account account) {
                accountName = account.getAccountName();
            }
            else if (authentication.getPrincipal() instanceof String principal) {
                accountName = principal;
            }
        }
        
        return accountName;
    }
    
}
