package com.nozma.core.service;

import com.nozma.core.dto.request.PagePayload;
import com.nozma.core.dto.request.UpdateAccountPayload;
import com.nozma.core.dto.response.AccountDetailResponse;
import com.nozma.core.dto.response.AccountPageResponse;
import com.nozma.core.entity.account.Account;
import jakarta.validation.Valid;

public interface AccountService {
    Account findByAccountName(String accountName);
    
    AccountPageResponse getAccountList(PagePayload pagePayload);
    
    AccountDetailResponse getAccountDetail(Long accountId);
    
    void updateAccount(Long accountId, @Valid UpdateAccountPayload payload);
    
    void deactivateAccount(long accountId);
}
