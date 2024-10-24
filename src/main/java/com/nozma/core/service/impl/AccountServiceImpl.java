package com.nozma.core.service.impl;

import com.nozma.core.dto.request.EditableAccountPayload;
import com.nozma.core.dto.response.AccountColumnResponse;
import com.nozma.core.dto.response.AccountDetailResponse;
import com.nozma.core.dto.response.AccountPageResponse;
import com.nozma.core.dto.response.AccountViewResponse;
import com.nozma.core.dto.response.EditableAccountResponse;
import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.AccountColumn;
import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.TokenType;
import com.nozma.core.exception.AccountNotFoundException;
import com.nozma.core.projection.AccountView;
import com.nozma.core.repository.AccountColumnRepository;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.service.AccountService;
import com.nozma.core.service.TokenService;
import com.nozma.core.util.DateUtil;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    private final TokenService tokenService;
    
    @Override
    @Transactional(readOnly = true)
    public Account findByAccountName(String accountName) {
        
        return accountRepository.findOneByAccountName(accountName)
                .orElseThrow(
                        AccountNotFoundException::new
                );
    }
    
    @Override
    @Transactional(readOnly = true)
    public AccountPageResponse getAccountList(Pageable pageable, String searchName) {
        
        Page<AccountView> accountViews = accountRepository.fetchAllByPaging(pageable, searchName);
        
        if (accountViews.isEmpty()) {
            throw new NoResultException();
        }
        
        List<AccountColumn> accountColumns = accountColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        
        Function<AccountView, AccountViewResponse> convert = accountView ->
                new AccountViewResponse(
                        accountView.getId(),
                        accountView.getAccountName(),
                        accountView.getEmail(),
                        accountView.getUser().getFullName(),
                        accountView.getUser().getBirthdate(),
                        DateUtil.formatDateTime(
                                accountView.getCreatedDate(),
                                DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND
                        ),
                        DateUtil.formatDateTime(
                                accountView.getUpdatedDate(),
                                DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND
                        ),
                        accountView.getStatus()
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
                accountViews.getTotalElements(),
                accountColumns
                        .stream()
                        .map(convertToAccountColumnResponse)
                        .toList(),
                accountViews
                        .getContent()
                        .stream()
                        .map(convert)
                        .toList()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public AccountDetailResponse getAccountDetail(long accountId) {
        
        var account = findAccountById(accountId);
        
        if (RecordStatus.DELETED.equals(account.getStatus())) {
            throw new DisabledException(Strings.EMPTY);
        }
        
        if (RecordStatus.LOCKED.equals(account.getStatus())) {
            throw new LockedException(Strings.EMPTY);
        }
        
        List<AccountColumn> accountColumns = accountColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        
        Function<AccountColumn, AccountColumnResponse> convertToAccountColumnResponse =
                accountColumnView -> new AccountColumnResponse(
                        accountColumnView.getCode(),
                        accountColumnView.getName(),
                        accountColumnView.getType()
                );
        
        return new AccountDetailResponse(
                accountId,
                account.getAccountName(),
                account.getEmail(),
                account.getUser().getFirstName(),
                account.getUser().getLastName(),
                account.getUser().getBirthdate(),
                DateUtil.formatDateTime(account.getCreatedDate(), DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND),
                DateUtil.formatDateTime(account.getUpdatedDate(), DateUtil.YEAR_MONTH_HOUR_MINUTE_SECOND),
                account.getStatus().toString(),
                accountColumns
                        .stream()
                        .map(convertToAccountColumnResponse)
                        .toList()
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EditableAccountResponse updateAccount(Long accountId, EditableAccountPayload payload)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        Account account = validatesAndGetsAccount(accountId);
        
        clearUserCaches(account);
        
        account.setAccountName(payload.getAccountName().trim());
        
        account.setEmail(payload.getEmail().trim());
        
        account.getUser().setFirstName(payload.getFirstName().trim());
        
        account.getUser().setLastName(payload.getLastName().trim());
        
        account.getUser().setBirthdate(payload.getBirthdate().trim());
        
        var accountDetails = JwtAccountDetails.builder()
                .account(Account.builder()
                        .id(account.getId())
                        .accountName(account.getAccountName())
                        .build())
                .build();
        
        var profileToken = tokenService.generateToken(accountDetails, TokenType.PROFILE_TOKEN);
        var refreshToken = tokenService.generateToken(accountDetails, TokenType.REFRESH_TOKEN);
        
        accountRepository.save(account);
        
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
                profileToken,
                refreshToken
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivateAccount(long accountId) {
        var account = validatesAndGetsAccount(accountId);
        
        clearUserCaches(account);
        
        // Deactivate account
        account.setStatus(RecordStatus.DELETED);
        
        // Deactivate user
        account.getUser().setStatus(RecordStatus.DELETED);
        
        // TODO: Deactivate login histories
        accountRepository.save(account);
    }
    
    private Account validatesAndGetsAccount(long accountId) {
        var account = findAccountById(accountId);
        
        if (RecordStatus.DELETED.equals(account.getStatus())) {
            throw new DisabledException(Strings.EMPTY);
        }
        
        if (RecordStatus.LOCKED.equals(account.getStatus())) {
            throw new LockedException(Strings.EMPTY);
        }
        
        return account;
    }
    
    @Override
    public Account findAccountById(long accountId) {
        return accountRepository.findOneById(accountId).orElseThrow(
                AccountNotFoundException::new
        );
    }
    
    private void clearUserCaches(Account account) {
        Objects.requireNonNull(cacheManager.getCache(AccountRepository.ACCOUNT_BY_NAME))
                .evict(account.getAccountName());
        
        Objects.requireNonNull(cacheManager.getCache(AccountRepository.ACCOUNT_BY_ID))
                .evict(account.getId());
    }
}
