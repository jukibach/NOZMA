package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.request.ChangePasswordPayload;
import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.request.ReissueTokenPayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.util.ResponseEntityUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AccountService accountService;
    
    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntityUtil.createSuccessResponse(accountService.login(request));
    }
    
    @PostMapping(value = "/register/users") // Based on AWS
    public ResponseEntity<ApiResponse> registerNewUserAccount(@RequestBody UserRegistrationRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntityUtil.createSuccessResponse(accountService.registerNewAccount(request));
    }
    
    @PostMapping(value = "/accounts/listAccount")
    public ResponseEntity<ApiResponse> getAccountList(@RequestBody PagePayload request) {
        return ResponseEntityUtil.createSuccessResponse(accountService.getAccountList(request));
    }
    
    @PostMapping(value = "/reissue-token")
    public ResponseEntity<ApiResponse> reissueToken(@Valid @RequestBody ReissueTokenPayload payload) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return ResponseEntityUtil.createSuccessResponse(accountService.reissueToken(payload));
    }
    
    @PostMapping(value = "/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordPayload payload) {
        accountService.changePassword(payload);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
    
    @PostMapping(value = "/reissue-password")
    public ResponseEntity<ApiResponse> reissueToken(@Valid @RequestParam String accountName) {
        accountService.reissuePassword(accountName);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
}
