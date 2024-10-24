package com.nozma.core.controller;

import com.nozma.core.constant.ApiURL;
import com.nozma.core.constant.Privileges;
import com.nozma.core.dto.request.PagePayload;
import com.nozma.core.dto.request.UpdateAccountPayload;
import com.nozma.core.dto.response.ApiResponse;
import com.nozma.core.service.AccountService;
import com.nozma.core.util.ResponseEntityUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(ApiURL.ROOT_PATH)
public class AccountController {
    private final AccountService accountService;
    
    @PreAuthorize("hasAuthority('" + Privileges.GET_USERS + "')")
    @PostMapping(value = ApiURL.GET_ACCOUNTS)
    public ResponseEntity<ApiResponse> getAccountList(@RequestBody PagePayload request) {
        return ResponseEntityUtil.createSuccessfulOkResponse(accountService.getAccountList(request));
    }
    
    @PreAuthorize("hasAuthority('" + Privileges.GET_USER_DETAIL + "')")
    @GetMapping(value = ApiURL.ACCOUNT_BY_ID)
    public ResponseEntity<ApiResponse> getAccountDetail(@PathVariable(value = "id") Long accountId) {
        return ResponseEntityUtil.createSuccessfulOkResponse(accountService.getAccountDetail(accountId));
    }
    
    @PreAuthorize("hasAuthority('" + Privileges.UPDATE_USER + "')")
    @PutMapping(value = ApiURL.ACCOUNT_BY_ID)
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable(value = "id") Long accountId,
                                                     @Valid @RequestBody UpdateAccountPayload payload) {
        accountService.updateAccount(accountId, payload);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
    
    @PreAuthorize("hasAuthority('" + Privileges.DELETE_USER + "')")
    @DeleteMapping(value = ApiURL.ACCOUNT_BY_ID)
    public ResponseEntity<ApiResponse> deactivateAccount(@PathVariable(value = "id") long accountId) {
        accountService.deactivateAccount(accountId);
        return ResponseEntityUtil.createSuccessResponseWithoutData();
    }
}