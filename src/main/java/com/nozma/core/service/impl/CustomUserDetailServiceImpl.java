package com.nozma.core.service.impl;

import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.exception.AccountNotFoundException;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.repository.RolePrivilegeRepository;
import com.nozma.core.service.LoginHistoryService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final LoginHistoryService loginHistoryService;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        var account = accountRepository.findOneByAccountName(accountName)
                .orElseThrow(AccountNotFoundException::new);
        
        if (RecordStatus.LOCKED.equals(account.getStatus())) {
            loginHistoryService.unlockWhenExpired(account);
            throw new LockedException(Strings.EMPTY);
        }
        
        if (RecordStatus.DELETED.equals(account.getStatus())) {
            throw new DisabledException(Strings.EMPTY);
        }
        
        var passwordExpiredDate = account.getToDate();
        var passwordDayLeft = Objects.isNull(passwordExpiredDate)
                ? 0L
                : DateUtil.getCurrentDate().until(passwordExpiredDate, ChronoUnit.DAYS) + 1;
        
        if (passwordExpiredDate.isBefore(DateUtil.getCurrentDate()) || passwordDayLeft == 0) {
            throw new CredentialsExpiredException(Strings.EMPTY);
        }
        
        if (Objects.isNull(account.getUser())) {
            throw new BusinessException(StatusAndMessage.USER_DOES_NOT_EXIST);
        }
        
        // get privileges
        List<String> privileges = rolePrivilegeRepository.findPrivilegeNamesByRoleId(account.getRole().getId());
        
        return new JwtAccountDetails(
                account,
                account.getUser(),
                account.getRole().getName(),
                privileges
        );
    }
    
}
