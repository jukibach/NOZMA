package com.nozma.core.service.impl;

import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.repository.RolePrivilegeRepository;
import com.nozma.core.repository.UserRepository;
import com.nozma.core.service.LoginHistoryService;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.DateUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
            throw new LockedException(Strings.EMPTY);
        }
        if (RecordStatus.INACTIVE.equals(account.getStatus())) {
            throw new DisabledException(Strings.EMPTY);
        }
        var passwordExpiredDate = account.getToDate();
        var passwordDayLeft = CommonUtil.isNullOrEmpty(passwordExpiredDate) ? 0L :
                DateUtil.getCurrentDate().until(passwordExpiredDate, ChronoUnit.DAYS) + 1;
        if (passwordExpiredDate.isBefore(DateUtil.getCurrentDate()) || passwordDayLeft == 0) {
            throw new CredentialsExpiredException(Strings.EMPTY);
        }
        var user = userRepository.findById(account.getUser().getId());
        if (user.isEmpty()) {
            throw new BusinessException(StatusAndMessage.USER_DOES_NOT_EXIST);
        }
        
        // get privileges
        List<String> privileges = rolePrivilegeRepository.findPrivilegeNamesByRoleId(account.getRole().getId());
        
        return new JwtAccountDetails(account, user.get(), account.getRole().getName(), privileges);
    }
    
}
