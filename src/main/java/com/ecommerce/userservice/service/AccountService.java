package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.response.AccountDetailResponse;
import com.ecommerce.userservice.dto.response.AccountPageResponse;
import com.ecommerce.userservice.entity.Account;

public interface AccountService {
    Account findByAccountName(String accountName);
    
    AccountPageResponse getAccountList(PagePayload pagePayload);
    
    AccountDetailResponse getAccountDetail(Long accountId);
}
