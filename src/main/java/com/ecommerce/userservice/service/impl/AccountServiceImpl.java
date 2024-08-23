package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.response.AccountDetailResponse;
import com.ecommerce.userservice.dto.response.AccountPageResponse;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mybatis.mapper.MybatisUserMapper;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.SecurityUtil;
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
}
