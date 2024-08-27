package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.entity.Account;

public interface LoginHistoryService {
    void resetFailedAttempts(String accountName);
    
    void lockWhenMultipleFailedAttempts(LoginRequest loginRequest, Account account);
    
    void unlockWhenExpired(Account account);
}
