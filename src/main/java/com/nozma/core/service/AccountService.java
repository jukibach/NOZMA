package com.nozma.core.service;

import com.nozma.core.dto.request.UpdateAccountPayload;
import com.nozma.core.dto.response.AccountDetailResponse;
import com.nozma.core.dto.response.AccountPageResponse;
import com.nozma.core.dto.response.EditableAccountResponse;
import com.nozma.core.entity.account.Account;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    Account findByAccountName(String accountName);
    
    AccountPageResponse getAccountList(Pageable pageable, String searchName);
    
    EditableAccountResponse getAccountDetail(long accountId);
    
    AccountDetailResponse updateAccount(Long accountId, @Valid UpdateAccountPayload payload);
    
    void deactivateAccount(long accountId);
}
