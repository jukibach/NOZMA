package com.ecommerce.userservice.entity;

import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Builder
@Data
public class JwtAccountDetails implements UserDetails {
    
    private Account account;
    private User user;
    private List<AccountRole> accountRoles;
    private List<RolePrivilege> privileges;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(getUserRoles().stream(), getPrivileges().stream()).map(SimpleGrantedAuthority::new).toList();
    }
    
    public List<String> getUserRoles() {
        if (CommonUtil.isNullOrEmpty(accountRoles)) {
            return List.of();
        }
        return accountRoles.stream().map(AccountRole::getRole).map(Role::getName).distinct().toList();
    }
    
    @Override
    public String getPassword() {
        return account.getPassword();
    }
    
    @Override
    public String getUsername() {
        return account.getAccountName();
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !account.isLocked();
    }
    
    @Override
    public boolean isEnabled() {
        return RecordStatus.ACTIVE.equals(account.getStatus());
    }
    
    public List<String> getPrivileges() {
        if (CommonUtil.isNullOrEmpty(privileges)) {
            return List.of();
        }
        return privileges.stream().map(RolePrivilege::getPrivilege).map(Privilege::getName).distinct().toList();
    }
}
