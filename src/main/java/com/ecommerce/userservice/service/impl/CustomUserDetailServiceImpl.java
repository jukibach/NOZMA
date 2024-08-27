package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.repository.RolePrivilegeRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.LoginHistoryService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {
    
    private final AccountRepository accountRepository;
    
    private final UserRepository userRepository;
    
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
            throw new BusinessException(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS);
        }
        if (RecordStatus.INACTIVE.equals(account.getStatus())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED);
        }
        var passwordExpiredDate = account.getToDate();
        var passwordDayLeft = CommonUtil.isNullOrEmpty(account.getToDate()) ? 0L :
                DateUtil.getCurrentDate().until(passwordExpiredDate, ChronoUnit.DAYS) + 1;
        if (account.getToDate().isAfter(DateUtil.getCurrentDate()) || passwordDayLeft == 0) {
            throw new BusinessException(StatusAndMessage.PASSWORD_EXPIRED);
        }
        var user = userRepository.findById(account.getUserId());
        if (user.isEmpty()) {
            throw new BusinessException(StatusAndMessage.USER_DOES_NOT_EXIST);
        }
        
        // get privileges
        List<String> privileges = rolePrivilegeRepository.findPrivilegeNamesByRoleId(account.getRoleId());
        
        return new JwtAccountDetails(account, user.get(), account.getRoleName(), privileges);
    }
    
}
