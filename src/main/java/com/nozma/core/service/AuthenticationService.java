package com.nozma.core.service;

import com.nozma.core.dto.request.ChangePasswordPayload;
import com.nozma.core.dto.request.LoginRequest;
import com.nozma.core.dto.request.ReissueTokenPayload;
import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.dto.response.LoginResponse;
import com.nozma.core.dto.response.ReissueTokenResponse;
import com.nozma.core.dto.response.UserRegistrationResponse;
import jakarta.validation.Valid;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    UserRegistrationResponse registerNewAccount(UserRegistrationRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    ReissueTokenResponse reissueToken(@Valid ReissueTokenPayload payload) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;
    
    void logout(String token);
    
    void changePassword(@Valid ChangePasswordPayload payload);
}
