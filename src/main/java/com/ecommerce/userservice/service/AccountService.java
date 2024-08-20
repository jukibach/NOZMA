package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.ChangePasswordPayload;
import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.request.ReissueTokenPayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.AccountPageResponse;
import com.ecommerce.userservice.dto.response.LoginResponse;
import com.ecommerce.userservice.dto.response.ReissueTokenResponse;
import com.ecommerce.userservice.dto.response.UserRegistrationResponse;
import com.ecommerce.userservice.entity.Account;
import jakarta.validation.Valid;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AccountService {
    Account findByAccountName(String accountName);
    
    AccountPageResponse getAccountList(PagePayload pagePayload);
    
    LoginResponse login(LoginRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    UserRegistrationResponse registerNewAccount(UserRegistrationRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    void reissuePassword(String accountName);
    
    ReissueTokenResponse reissueToken(@Valid ReissueTokenPayload payload) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;
    
    void logout(String token);
    
    void changePassword(@Valid ChangePasswordPayload payload);
}
