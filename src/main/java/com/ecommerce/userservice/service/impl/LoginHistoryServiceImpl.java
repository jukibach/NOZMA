package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.cache.CacheStore;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final AccountRepository accountRepository;
    private final CacheStore<String, Integer> loginAttempt;
    
    @Override
    public void saveFailedLogin(LoginRequest loginRequest, Account account, String failureReason) {
        
        // if this is the first failed login or there has been less than 5 failed login attempts
        if (CommonUtil.isNullOrEmpty(loginAttempt.get(loginRequest.accountName()))
                || loginAttempt.get(loginRequest.accountName()) < 5) {
            var failedAttemptNumber = loginAttempt.getOrDefault(loginRequest.accountName(), 0);
            loginAttempt.put(loginRequest.accountName(), failedAttemptNumber + 1);
        }
        
        var failedLogin = LoginHistory.builder()
                .accountId(account.getId())
                .userId(account.getUserId())
                .accountName(loginRequest.accountName())
                .ipAddress(loginRequest.ipAddress())
                .isLoginSuccessful(false)
                .loginTimestamp(DateUtil.toLocalDateTime(loginRequest.loginTimestamp()))
                .deviceType(DeviceType.valueOf(loginRequest.deviceType()))
                .deviceOs(loginRequest.deviceOS())
                .browserName(loginRequest.browserName())
                .browserVersion(loginRequest.browserVersion())
                .failureReason(failureReason)
                .build();
        loginHistoryRepository.save(failedLogin);
        
        // Account will be locked if there are exactly 5 failed attempts
        if (loginAttempt.get(account.getAccountName()) == 5) {
            account.setLocked(true);
            accountRepository.save(account);
        }
    }
    
    @Override
    @Transactional
    public void saveSuccessfulLogin(LoginRequest loginRequest, Account account) {
        // Delete failed login attempts
        // What if other people fail login from another device when the server is deleting these failed records ?

        loginHistoryRepository.deactivateByAccountName(loginRequest.accountName());
        loginAttempt.remove(loginRequest.accountName());
        
        var successfulLogin = LoginHistory.builder()
                .accountId(account.getId())
                .userId(account.getUserId())
                .accountName(loginRequest.accountName())
                .ipAddress(loginRequest.ipAddress())
                .loginTimestamp(DateUtil.toLocalDateTime(loginRequest.loginTimestamp()))
                .deviceType(DeviceType.valueOf(loginRequest.deviceType()))
                .deviceOs(loginRequest.deviceOS())
                .browserName(loginRequest.browserName())
                .browserVersion(loginRequest.browserVersion())
                .isLoginSuccessful(true)
                .build();
        loginHistoryRepository.save(successfulLogin);
    }
}
