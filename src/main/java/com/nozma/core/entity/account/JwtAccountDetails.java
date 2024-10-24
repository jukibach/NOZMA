package com.nozma.core.entity.account;

import com.nozma.core.enums.RecordStatus;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Builder
@Data
public class JwtAccountDetails implements UserDetails {
    
    private transient Account account;
    private transient User user;
    private String role;
    private List<String> privileges;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(Stream.of(getUserRole()), getPrivileges().stream()).map(SimpleGrantedAuthority::new).toList();
    }
    
    public String getUserRole() {
        return role;
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
    
    @Override
    public boolean isCredentialsNonExpired() {
        if (CommonUtil.isNullOrEmpty(account.getToDate())) {
            return false;
        }
        return account.getToDate().isAfter(DateUtil.getCurrentDate()) || getPasswordDayLeft() == 0;
    }
    
    public Long getAccountId() {
        return account.getId();
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !RecordStatus.LOCKED.equals(account.getStatus());
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
    
    public Long getPasswordDayLeft() {
        if (CommonUtil.isNullOrEmpty(account.getToDate())) {
            return 0L;
        }
        var passwordExpiredDate = account.getToDate();
        return DateUtil.getCurrentDate().until(passwordExpiredDate, ChronoUnit.DAYS) + 1;
    }
}
