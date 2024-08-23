package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.repository.AccountRoleRepository;
import com.ecommerce.userservice.repository.RolePrivilegeRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.LoginHistoryService;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {
    
    private final AccountRepository accountRepository;
    
    private final UserRepository userRepository;
    
    private final AccountRoleRepository accountRoleRepository;
    
    private final RolePrivilegeRepository rolePrivilegeRepository;
    
    private final LoginHistoryService loginHistoryService;
    
    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        var account = accountRepository.findByAccountName(accountName);
        
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        if (account.isLocked()) {
            loginHistoryService.unlockWhenExpired(account);
        }
        
        var user = userRepository.findById(account.getUserId());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist!");
        }
        
        // get roles
        List<Object[]> roles = accountRoleRepository.findRoleIdAndNameByAccountId(account.getId());
        List<String> roleNames = roles.stream().map(object -> (String) object[1]).toList();
        List<Integer> roleIds = roles.stream().map(object -> (Integer) object[0]).toList();
        
        // get privileges
        List<String> privileges = rolePrivilegeRepository.findPrivilegeNamesByRoleIds(roleIds);
        
        return new JwtAccountDetails(account, user.get(), roleNames, privileges);
    }
}
