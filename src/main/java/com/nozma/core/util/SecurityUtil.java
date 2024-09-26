package com.nozma.core.util;

import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.JwtAccountDetails;
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
            else if (authentication.getPrincipal() instanceof JwtAccountDetails jwtAccountDetails) {
                accountName = jwtAccountDetails.getAccount().getAccountName();
            }
        }
        
        return accountName;
    }
    
    public static Long getCurrentAccountId() {
        var principal = (JwtAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (CommonUtil.isNullOrEmpty(principal)) {
            return null;
        }
        return principal.getAccountId();
    }
    
    public static String getCurrentRole() {
        var principal = (JwtAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (CommonUtil.isNullOrEmpty(principal)) {
            return null;
        }
        return principal.getUserRole();
    }
}
