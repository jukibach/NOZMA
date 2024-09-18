package com.nozma.core.service.impl;

import com.nozma.core.cache.CacheStore;
import com.nozma.core.config.ApplicationProperties;
import com.nozma.core.dto.request.LoginRequest;
import com.nozma.core.entity.account.Account;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.service.LoginHistoryService;
import com.nozma.core.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final AccountRepository accountRepository;
    private final CacheStore<String, Integer> loginFailedAttemptCache;
    private final ApplicationProperties applicationProperties;
    
    @Override
    public void resetFailedAttempts(String accountName) {
        Integer failedAttempts = getFailedAttempts(accountName);
        if (CommonUtil.isNonNullOrNonEmpty(failedAttempts) && failedAttempts > 0) {
            putFailedAttempts(accountName, 0);
            log.info("Login failed attempts for {} have been reset", accountName);
        }
    }
    
    private Integer getFailedAttempts(String accountName) {
        return loginFailedAttemptCache.get(accountName);
    }
    
    private void increaseFailedAttempts(String accountName, Integer currentFailedAttempts) {
        int newFailedAttempts = 1;
        if (CommonUtil.isNonNullOrNonEmpty(currentFailedAttempts)) {
            newFailedAttempts = currentFailedAttempts + 1;
        }
        putFailedAttempts(accountName, newFailedAttempts);
    }
    
    private void putFailedAttempts(String accountName, Integer failedAttempts) {
        loginFailedAttemptCache.add(accountName, failedAttempts);
    }
    
    @Override
    public void lockWhenMultipleFailedAttempts(LoginRequest loginRequest, Account account) {
        
        Integer currentFailedAttempts = getFailedAttempts(account.getAccountName());
        
        // if this is the first failed login or there has been less than 5 failed login attempts
        if (CommonUtil.isNullOrEmpty(currentFailedAttempts)
                || currentFailedAttempts < applicationProperties.getMaxFailedAttempts() - 1) {
            increaseFailedAttempts(account.getAccountName(), currentFailedAttempts);
            return;
        }
        
        // Lock account
        account.setLocked(true);
        account.setLastLocked(LocalDateTime.now());
        accountRepository.save(account);
        
        log.info("Account {} has been lock after {} failed attempts", loginRequest.getAccountName(),
                applicationProperties.getMaxFailedAttempts());
        // Reset failed attempts
        resetFailedAttempts(account.getAccountName());
    }
    
    @Override
    public void unlockWhenExpired(Account account) {
        LocalDateTime unlockTime = account.getLastLocked().plusMinutes(applicationProperties.getLockTimeInMinute());
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(unlockTime)) {
            account.setLocked(false);
            account.setLastLocked(null);
            log.info("Account {} has been unlocked after lock time expired!", account.getAccountName());
            resetFailedAttempts(account.getAccountName());
        }
    }
}
