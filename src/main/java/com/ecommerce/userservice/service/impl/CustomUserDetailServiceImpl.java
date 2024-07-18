package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {
    
    private final AccountRepository accountRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        var account = accountRepository.findByAccountName(accountName);
        
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new UsernameNotFoundException("Account " + accountName +" does not exist!");
        }
        
        return new User(accountName, passwordEncoder.encode(account.getPassword()), null);
    }
}
