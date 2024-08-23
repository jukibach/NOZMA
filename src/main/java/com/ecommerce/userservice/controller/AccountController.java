package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.constant.Privileges;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    
    @PreAuthorize("hasAuthority('" + Privileges.GET_USERS +  "')")
    @PostMapping(value = "/listAccount")
    public ResponseEntity<ApiResponse> getAccountList(@RequestBody PagePayload request) {
        return ResponseEntityUtil.createSuccessResponse(accountService.getAccountList(request));
    }
    
    @PreAuthorize("hasAuthority('" + Privileges.GET_USER_DETAIL +  "')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResponse> getAccountDetail(@PathVariable(value = "id") Long accountId) {
        return ResponseEntityUtil.createSuccessResponse(accountService.getAccountDetail(accountId));
    }
}
