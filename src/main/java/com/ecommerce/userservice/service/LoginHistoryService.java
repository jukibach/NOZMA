package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.entity.Account;

public interface LoginHistoryService {
    void saveFailedLogin(LoginRequest request, Account account, String failureReason);
    
    void saveSuccessfulLogin(LoginRequest loginRequest, Account account);
}
