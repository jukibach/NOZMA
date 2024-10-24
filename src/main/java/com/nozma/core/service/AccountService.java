package com.nozma.core.service;

import com.nozma.core.dto.request.EditableAccountPayload;
import com.nozma.core.dto.response.AccountDetailResponse;
import com.nozma.core.dto.response.AccountPageResponse;
import com.nozma.core.dto.response.EditableAccountResponse;
import com.nozma.core.entity.account.Account;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AccountService {
    Account findByAccountName(String accountName);
    
    AccountPageResponse getAccountList(Pageable pageable, String searchName);
    
    AccountDetailResponse getAccountDetail(long accountId);
    
    EditableAccountResponse updateAccount(Long accountId, @Valid EditableAccountPayload payload)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    void deactivateAccount(long accountId);
    
    Account findAccountById(long accountId) ;
}
