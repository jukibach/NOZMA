package com.ecommerce.userservice.entity;

import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
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
    
    private transient Account account;
    private transient User user;
    private List<String> roles;
    private List<String> privileges;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(getUserRoles().stream(), getPrivileges().stream()).map(SimpleGrantedAuthority::new).toList();
    }
    
    public List<String> getUserRoles() {
        return roles;
    }
    
    @Override
    public String getPassword() {
        return account.getPassword();
    }
    
    @Override
    public String getUsername() {
        return null;
    }
    
    public String getAccountName() {
        return account.getAccountName();
    }
    
    public Long getAccountId() {
        return account.getId();
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
        return privileges.stream().distinct().toList();
    }
    
    public String getRole() {
        return roles.stream().findFirst().orElse(Strings.EMPTY);
    }
}
