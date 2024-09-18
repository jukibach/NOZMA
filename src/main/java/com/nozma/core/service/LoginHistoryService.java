package com.nozma.core.service;

import com.nozma.core.dto.request.LoginRequest;
import com.nozma.core.entity.account.Account;

public interface LoginHistoryService {
    void resetFailedAttempts(String accountName);
    
    void lockWhenMultipleFailedAttempts(LoginRequest loginRequest, Account account);
    
    void unlockWhenExpired(Account account);
}
