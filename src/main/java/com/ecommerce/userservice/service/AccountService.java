package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.AccountPageResponse;
import com.ecommerce.userservice.dto.response.LoginResponse;
import com.ecommerce.userservice.dto.response.UserRegistrationResponse;
import com.ecommerce.userservice.entity.Account;

public interface AccountService {
    Account findByAccountName(String accountName);
    
    AccountPageResponse getAccountList(PagePayload pagePayload);
    
    LoginResponse login(LoginRequest request);
    
    UserRegistrationResponse registerNewAccount(UserRegistrationRequest request);
}
