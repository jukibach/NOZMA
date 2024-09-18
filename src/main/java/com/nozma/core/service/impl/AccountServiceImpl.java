package com.nozma.core.service.impl;

import com.nozma.core.dto.request.PagePayload;
import com.nozma.core.dto.request.UpdateAccountPayload;
import com.nozma.core.dto.response.AccountDetailResponse;
import com.nozma.core.dto.response.AccountPageResponse;
import com.nozma.core.entity.account.Account;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.mybatis.mapper.MybatisUserMapper;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.service.AccountService;
import com.nozma.core.service.UserService;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final MybatisUserMapper mybatisUserMapper;
    private final UserService userService;
    
    @Override
    public Account findByAccountName(String accountName) {
        var account = accountRepository.findByAccountName(accountName);
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        return account;
    }
    
    @Override
    public AccountPageResponse getAccountList(PagePayload pagePayload) {
        if (CommonUtil.isNullOrEmpty(SecurityUtil.getCurrentAccountName())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        if (!pagePayload.visibleColumns().contains(Account.Fields.accountName)) {
            throw new BusinessException("Column Account Name must be selected");
        }
        
        var accountResponses = mybatisUserMapper.selectFields(pagePayload);
        
        if (CommonUtil.isNullOrEmpty(accountResponses)) {
            throw new BusinessException("Account Name must be selected");
        }
        
        return new AccountPageResponse(pagePayload.pageSize(), pagePayload.pageIndex(), accountResponses.size(),
                accountResponses);
    }
    
    @Override
    public AccountDetailResponse getAccountDetail(Long accountId) {
        
        if (!Objects.equals(SecurityUtil.getCurrentAccountId(), accountId)) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        
        var account = mybatisUserMapper.getAccountDetail(accountId);
        
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        
        if (RecordStatus.INACTIVE.toString().equals(account.getStatus())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED);
        }
        
        if (account.isLocked()) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS);
        }
        
        return account;
    }
    
    @Override
    @Transactional
    public void updateAccount(Long accountId, UpdateAccountPayload payload) {
        var account = getAccountForUpdateAndDelete(accountId);
        
        if (CommonUtil.isNonNullOrNonEmpty(payload.getAccountName())) {
            account.setAccountName(payload.getAccountName());
        }
        
        if (CommonUtil.isNonNullOrNonEmpty(payload.getEmail())) {
            account.setEmail(payload.getEmail());
        }
        
        accountRepository.save(account);
        userService.updateUser(payload, account.getUser().getId());
    }
    
    @Override
    @Transactional
    public void deactivateAccount(long accountId) {
        var account = getAccountForUpdateAndDelete(accountId);
        
        // Deactivate account
        account.setStatus(RecordStatus.INACTIVE);
        
        // Deactivate user
        userService.deleteUser(account.getUser().getId());
        
        // Deactivate login histories
        
        accountRepository.save(account);
    }
    
    private Account getAccountForUpdateAndDelete(long accountId) {
        if (!Objects.equals(SecurityUtil.getCurrentAccountId(), accountId)) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        
        var account = accountRepository.findById(accountId);
        
        if (account.isEmpty()) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        
        if (RecordStatus.INACTIVE.equals(account.get().getStatus())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED);
        }
        
        if (account.get().isLocked()) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS);
        }
        
        return account.get();
    }
}
