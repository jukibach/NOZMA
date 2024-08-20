package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.entity.AccountRole;
import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.entity.Role;
import com.ecommerce.userservice.entity.RolePrivilege;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.LoginHistoryService;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {
    
    private final AccountRepository accountRepository;
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final LoginHistoryService loginHistoryService;
    
    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        var account = accountRepository.findByAccountName(accountName);
        
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new UsernameNotFoundException("Account " + accountName +" does not exist!");
        }
        if (account.isLocked()) {
            loginHistoryService.unlockWhenExpired(account);
        }
        
        var user = userRepository.findById(account.getUserId());
        
        List<AccountRole> accountRoles = account.getAccountRoles();
        
        List<RolePrivilege> privileges = new ArrayList<>();
        if (CommonUtil.isNonNullOrNonEmpty(accountRoles)) {
            privileges = accountRoles.stream()
                    .map(AccountRole::getRole)
                    .map(Role::getRolePrivileges)
                    .flatMap(List::stream).toList();
        }
        return new JwtAccountDetails(account, user.get(), accountRoles, privileges);
    }
}
