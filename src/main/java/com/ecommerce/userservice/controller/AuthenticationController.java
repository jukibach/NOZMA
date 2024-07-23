package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AccountService accountService;
    
    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntityUtil.createSuccessResponse(accountService.login(request));
    }
    
    @PostMapping(value = "/register/users")
    public ResponseEntity<ApiResponse> registerNewUserAccount(@RequestBody UserRegistrationRequest request) {
        return ResponseEntityUtil.createSuccessResponse(accountService.registerNewAccount(request));
    }
    
    @PostMapping(value = "/accounts/listAccount")
    public ResponseEntity<ApiResponse> getAccountList(@RequestBody PagePayload request) {
        return ResponseEntityUtil.createSuccessResponse(accountService.getAccountList(request));
    }
    
    @PostMapping(value = "/register/sellers")
    public ResponseEntity<ApiResponse> registerNewSellerAccount(@RequestBody UserRegistrationRequest request) {
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
}
