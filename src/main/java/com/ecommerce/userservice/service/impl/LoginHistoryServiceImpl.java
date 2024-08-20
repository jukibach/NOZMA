package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.cache.CacheStore;
import com.ecommerce.userservice.config.ApplicationProperties;
import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.entity.LoginHistory;
import com.ecommerce.userservice.enums.DeviceType;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.repository.LoginHistoryRepository;
import com.ecommerce.userservice.service.LoginHistoryService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
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
    public boolean lockWhenMultipleFailedAttempts(LoginRequest loginRequest, Account account) {
        
        Integer currentFailedAttempts = getFailedAttempts(account.getAccountName());
        
        // if this is the first failed login or there has been less than 5 failed login attempts
        if (CommonUtil.isNullOrEmpty(currentFailedAttempts)
                || currentFailedAttempts < applicationProperties.getMaxFailedAttempts()) {
            increaseFailedAttempts(account.getAccountName(), currentFailedAttempts);
            return false;
        }
        
        // Lock account
        account.setLocked(true);
        account.setLastLocked(LocalDateTime.now());
        accountRepository.save(account);
        
        var failedLogin = LoginHistory.builder()
                .accountId(account.getId())
                .userId(account.getUserId())
                .accountName(loginRequest.getAccountName())
                .ipAddress(loginRequest.getIpAddress())
                .isLoginSuccessful(false)
                .loginTimestamp(DateUtil.toLocalDateTime(loginRequest.getLoginTimestamp()))
                .deviceType(DeviceType.valueOf(loginRequest.getDeviceType()))
                .deviceOs(loginRequest.getDeviceOS())
                .browserName(loginRequest.getBrowserName())
                .browserVersion(loginRequest.getBrowserVersion())
                .build();
        loginHistoryRepository.save(failedLogin);
        log.info("Account {} has been lock after {} failed attempts", loginRequest.getAccountName(),
                applicationProperties.getMaxFailedAttempts());
        // Reset failed attempts
        resetFailedAttempts(account.getAccountName());
        return true;
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
