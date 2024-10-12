package com.nozma.core.service.impl;

import com.nozma.core.dto.request.UpdateAccountPayload;
import com.nozma.core.dto.response.AccountColumnResponse;
import com.nozma.core.dto.response.AccountDetailResponse;
import com.nozma.core.dto.response.AccountPageResponse;
import com.nozma.core.dto.response.EditableAccountResponse;
import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.AccountColumn;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.projection.AccountDetail;
import com.nozma.core.repository.AccountColumnRepository;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.service.AccountService;
import com.nozma.core.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountColumnRepository accountColumnRepository;
    private final AccountRepository accountRepository;
    private final CacheManager cacheManager;
    
    @Override
    @Transactional(readOnly = true)
    public Account findByAccountName(String accountName) {
        
        return accountRepository.findOneByAccountName(accountName)
                .orElseThrow(() ->
                        new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST)
                );
    }
    
    @Override
    public AccountPageResponse getAccountList(Pageable pageable, String searchName) {
        
        Page<AccountDetail> accountDetails = accountRepository.fetchAllByPaging(pageable, searchName);
        
        if (accountDetails.isEmpty()) {
            throw new BusinessException("There are no account details!");
        }
        
        List<AccountColumn> accountColumns = accountColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        
        Function<AccountDetail, AccountDetailResponse> convert = accountDetail ->
                new AccountDetailResponse(
                        accountDetail.getId(),
                        accountDetail.getAccountName(),
                        accountDetail.getEmail(),
                        accountDetail.getUser().getFullName(),
                        accountDetail.getUser().getBirthdate(),
                        DateUtil.formatDateTime(
                                accountDetail.getCreatedDate(),
                                DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND
                        ),
                        DateUtil.formatDateTime(
                                accountDetail.getUpdatedDate(),
                                DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND
                        ),
                        accountDetail.getStatus(),
                        accountDetail.getIsLocked() ? "YES" : "NO"
                );
        
        Function<AccountColumn, AccountColumnResponse> convertToAccountColumnResponse =
                accountColumnView -> new AccountColumnResponse(
                accountColumnView.getCode(),
                accountColumnView.getName(),
                accountColumnView.getType()
        );
        
        return new AccountPageResponse(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                accountDetails.getTotalElements(),
                accountColumns
                        .stream()
                        .map(convertToAccountColumnResponse)
                        .toList(),
                accountDetails
                        .getContent()
                        .stream()
                        .map(convert)
                        .toList()
        );
    }
    
    @Override
    public EditableAccountResponse getAccountDetail(long accountId) {
        
        var account = findAccountById(accountId);
        
        if (RecordStatus.INACTIVE.equals(account.getStatus())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED);
        }
        
        if (account.isLocked()) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS);
        }
        
        return new EditableAccountResponse(
                accountId,
                account.getAccountName(),
                account.getEmail(),
                account.getUser().getFirstName(),
                account.getUser().getLastName(),
                account.getUser().getBirthdate(),
                DateUtil.formatDateTime(account.getCreatedDate(), DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND),
                DateUtil.formatDateTime(account.getUpdatedDate(), DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND),
                account.getStatus().toString(),
                account.isLocked() ? "YES" : "NO"
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountDetailResponse updateAccount(Long accountId, UpdateAccountPayload payload) {
        
        Account account = getAccountValidation(accountId);
        
        clearUserCaches(account);
        
        if (Strings.isNotBlank(payload.getAccountName())) {
            account.setAccountName(payload.getAccountName().trim());
        }
        
        if (Strings.isNotBlank(payload.getEmail())) {
            account.setEmail(payload.getEmail().trim());
        }
        
        if (Strings.isNotBlank(payload.getFirstName())) {
            account.getUser().setFirstName(payload.getFirstName().trim());
        }
        
        if (Strings.isNotBlank(payload.getLastName())) {
            account.getUser().setLastName(payload.getLastName().trim());
        }
        
        if (Strings.isNotBlank(payload.getBirthdate())) {
            account.getUser().setBirthdate(payload.getBirthdate().trim());
        }
        
        accountRepository.save(account);
        
        return new AccountDetailResponse(
                accountId,
                account.getAccountName(),
                account.getEmail(),
                account.getUser().getFullName(),
                account.getUser().getBirthdate(),
                DateUtil.formatDateTime(account.getCreatedDate(), DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND),
                DateUtil.formatDateTime(account.getUpdatedDate(), DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND),
                account.getStatus().toString(),
                account.isLocked() ? "YES" : "NO"
        );
        
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivateAccount(long accountId) {
        var account = getAccountValidation(accountId);
        
        clearUserCaches(account);
        
        // Deactivate account
        account.setStatus(RecordStatus.INACTIVE);
        
        // Deactivate user
        account.getUser().setStatus(RecordStatus.INACTIVE);
        
        // TODO: Deactivate login histories
        accountRepository.save(account);
    }
    
    private Account getAccountValidation(long accountId) {
        var account = findAccountById(accountId);
        
        if (RecordStatus.INACTIVE.equals(account.getStatus())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED);
        }
        
        if (account.isLocked()) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS);
        }
        
        return account;
    }
    
    public Account findAccountById(long accountId) {
        return accountRepository.findOneById(accountId).orElseThrow(() ->
                new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST)
        );
    }
    
    private void clearUserCaches(Account account) {
        Objects.requireNonNull(cacheManager.getCache(AccountRepository.ACCOUNT_BY_NAME))
                .evict(account.getAccountName());
        
        Objects.requireNonNull(cacheManager.getCache(AccountRepository.ACCOUNT_BY_ID))
                .evict(account.getId());
    }
}
