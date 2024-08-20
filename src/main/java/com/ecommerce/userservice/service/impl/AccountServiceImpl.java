package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.config.ApplicationProperties;
import com.ecommerce.userservice.dto.request.ChangePasswordPayload;
import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.request.ReissueTokenPayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.AccountPageResponse;
import com.ecommerce.userservice.dto.response.LoginResponse;
import com.ecommerce.userservice.dto.response.ReissueTokenResponse;
import com.ecommerce.userservice.dto.response.UserRegistrationResponse;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.entity.AccountRole;
import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.entity.PasswordHistory;
import com.ecommerce.userservice.entity.TokenDetail;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.enums.Role;
import com.ecommerce.userservice.enums.TokenType;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mapper.AccountMapper;
import com.ecommerce.userservice.mybatis.mapper.MybatisUserMapper;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.repository.AccountRoleRepository;
import com.ecommerce.userservice.repository.PasswordHistoryRepository;
import com.ecommerce.userservice.repository.RolePrivilegeRepository;
import com.ecommerce.userservice.repository.RoleRepository;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.service.LoginHistoryService;
import com.ecommerce.userservice.service.TokenService;
import com.ecommerce.userservice.service.UserService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.DateUtil;
import com.ecommerce.userservice.util.PasswordGeneratorUtil;
import com.ecommerce.userservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final LoginHistoryService loginHistoryService;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final UserService userService;
    private final MybatisUserMapper mybatisUserMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordGeneratorUtil passwordGeneratorUtil;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ApplicationProperties applicationProperties;
    private final RoleRepository roleRepository;
    
    @Override
    public Account findByAccountName(String accountName) {
        var account = accountRepository.findByAccountName(accountName);
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new BusinessException("""
                    Account %s does not exist""".formatted(accountName));
        }
        return account;
    }
    
    @Override
    public AccountPageResponse getAccountList(PagePayload pagePayload) {
        if (CommonUtil.isNullOrEmpty(SecurityUtil.getCurrentAccountName())) {
            throw new BusinessException("Account name does not exist");
        }
        if (!pagePayload.visibleColumns().contains(Account.Fields.accountName)) {
            throw new BusinessException("Column Account Name must be selected");
        }
        
        var accountResponses = mybatisUserMapper.selectFields(pagePayload);

//        if (CommonUtil.isNullOrEmpty(accountResponse)) {
//            throw new BusinessException("Account Name must be selected");
//        }
        
        return new AccountPageResponse(pagePayload.pageSize(), pagePayload.pageIndex(), accountResponses.size(),
                accountResponses);
    }
    
    @Override
    public LoginResponse login(LoginRequest request)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getAccountName(), request.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        JwtAccountDetails accountDetails = (JwtAccountDetails) authentication.getPrincipal();
        var profileToken = tokenService.generateToken(accountDetails, TokenType.PROFILE_TOKEN);
        var refreshToken = tokenService.generateToken(accountDetails, TokenType.REFRESH_TOKEN);
        
        var account = accountDetails.getAccount();
        
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new BusinessException("""
                        Account %s does not exist
                    """.formatted(request.getAccountName()));
        }
        
        if (account.isLocked() ||
                (RecordStatus.ACTIVE.equals(account.getStatus())
                        && loginHistoryService.lockWhenMultipleFailedAttempts(request, account))) {
            loginHistoryService.unlockWhenExpired(account);
            throw new BusinessException(
                    """
                            %s has failed login 5 times in a row! %s has been locked!
                            """.formatted(account.getAccountName(), account.getAccountName()));
        }
        
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            loginHistoryService.lockWhenMultipleFailedAttempts(request, account);
            throw new BusinessException(account.getAccountName()
                    .concat("'s password is incorrect!"));
        }
        loginHistoryService.resetFailedAttempts(account.getAccountName());
        return new LoginResponse(account.getAccountName(), account.getEmail(), profileToken, refreshToken,
                accountDetails.getPrivileges(), accountDetails.getUserRoles(),
                request.getLoginTimestamp());
    }
    
    @Override
    @Transactional
    public UserRegistrationResponse registerNewAccount(
            UserRegistrationRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (!Objects.equals(request.password(), request.confirmedPassword())) {
            throw new BusinessException("Password does not match!");
        }
        
        if (accountRepository.existsByAccountName(request.accountName())) {
            throw new BusinessException("Account name already exists!");
        }
        
        if (accountRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already exists!");
        }
        
        var newUser = userService.saveNewUser(request);
        var newAccount = accountMapper.registrationRequestToAccount(request);
        
        newAccount.setPassword(passwordEncoder.encode(request.password()));
        newAccount.setUserId(newUser.getId());
        newAccount.setLastLocked(LocalDateTime.now());
        newAccount.setFromDate(LocalDate.now()); // TODO
        newAccount.setToDate(LocalDate.now().plusDays(applicationProperties.getPasswordDurationInDays())); // TODO
        
        newAccount = accountRepository.save(newAccount);
        com.ecommerce.userservice.entity.Role role = roleRepository.findByIdAndStatus(Role.USER.getId(), RecordStatus.ACTIVE);
        accountRoleRepository.save(AccountRole.builder().account(newAccount).role(role).build());
        
        var accountDetails = JwtAccountDetails.builder()
                .account(Account.builder()
                        .id(newAccount.getId())
                        .accountName(newAccount.getAccountName())
                        .build())
                .build();
        var profileToken = tokenService.generateToken(accountDetails, TokenType.PROFILE_TOKEN);
        var refreshToken = tokenService.generateToken(accountDetails, TokenType.REFRESH_TOKEN);
        
        var privilegeNames = rolePrivilegeRepository.findPrivilegeNamesByRoleId(Role.USER.getId());
        
        // TODO:   Remember me
        //         Allow multiple login
        //         Adjust createdBy and updatedBy of the audit log
        //         Util
        //         Send email
        
        
        return new UserRegistrationResponse(newAccount.getAccountName(), newAccount.getEmail(),
                refreshToken, profileToken, Collections.singletonList(Role.USER.getRoleName()), privilegeNames);
    }
    
    @Override
    public void reissuePassword(String accountName) {
        var account = findByAccountName(accountName);
        
        if (account.isLocked()) {
            throw new BusinessException("Account ".concat(accountName).concat(" is locked"));
        }
        if (RecordStatus.INACTIVE.equals(account.getStatus())) {
            throw new BusinessException("Account ".concat(accountName).concat(" deleted"));
        }
        
        var generatedPassword = passwordGeneratorUtil.generatePassword();
        var encodedPassword = passwordEncoder.encode(generatedPassword);
        
        updatePasswordInTheDatabase(account, generatedPassword, Boolean.TRUE);
        // TODO : Send email
    }
    
    @Override
    public ReissueTokenResponse reissueToken(
            ReissueTokenPayload payload) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        if (tokenService.checkTokenExistsInBlackList(payload.getRefreshToken())) {
            throw new BusinessException("Token is invalid");
        }
        TokenDetail tokenDetail = tokenService.validateToken(payload.getRefreshToken());
        if (!tokenDetail.getAccountId().equals(payload.getAccountId())) {
            throw new BusinessException("Account name is invalid");
        }
        
        var accountDetails = JwtAccountDetails.builder()
                .account(Account.builder()
                        .id(payload.getAccountId())
                        .accountName(tokenDetail.getAccountName())
                        .build())
                .build();
        var profileToken = tokenService.generateToken(accountDetails, TokenType.PROFILE_TOKEN);
        var refreshToken = tokenService.generateToken(accountDetails, TokenType.REFRESH_TOKEN);
        
        tokenService.addTokenToBlackList(payload.getRefreshToken());
        return new ReissueTokenResponse(profileToken, refreshToken);
    }
    
    @Override
    public void logout(String token) {
        if (CommonUtil.isNonNullOrNonEmpty(token)) {
            tokenService.addTokenToBlackList(token);
        }
    }
    
    @Override
    public void changePassword(ChangePasswordPayload payload) {
        var currentAccount = findByAccountName(SecurityUtil.getCurrentAccount());
        
        checkChangePassword(payload, currentAccount);
        
        String encryptedPassword = passwordEncoder.encode(payload.getNewPassword());
        
        updatePasswordInTheDatabase(currentAccount, encryptedPassword, Boolean.FALSE);
        
    }
    
    private void updatePasswordInTheDatabase(Account account, String encryptedPassword, boolean isPasswordGenerated) {
        LocalDate fromDate = DateUtil.getCurrentDate();
        
        LocalDate toDate = DateUtil.plusDay(fromDate, applicationProperties.getPasswordDurationInDays());
        
        String oldPassword = account.getPassword();
        LocalDate fromDateInDB = account.getFromDate();
        
        account.setPassword(encryptedPassword);
        account.setFromDate(fromDate);
        account.setToDate(toDate);
        account.setPasswordGenerated(isPasswordGenerated);
        
        PasswordHistory passwordHistory = PasswordHistory.builder()
                .password(oldPassword)
                .fromDate(fromDateInDB)
                .toDate(fromDate)
                .accountId(account.getId())
                .build();
        
        accountRepository.save(account);
        passwordHistoryRepository.save(passwordHistory);
    }
    
    private void checkChangePassword(ChangePasswordPayload payload, Account account) {
        if (!BCrypt.checkpw(payload.getOldPassword(), account.getPassword())) {
            throw new BusinessException("Invalid password");
        }
        
        if (!payload.getNewPassword().equals(payload.getConfirmPassword())) {
            throw new BusinessException("New password does not match with confirm password");
        }
        
        if (payload.getOldPassword().equals(payload.getNewPassword())) {
            throw new BusinessException("New password matches with old password");
        }
        
        if (isNewPasswordEqualOldPasswords(account.getId(), payload.getNewPassword())) {
            throw new BusinessException("This new password has been recently used!");
        }
    }
    
    private boolean isNewPasswordEqualOldPasswords(Long accountId, String newPassword) {
        var oldPasswords = passwordHistoryRepository.findPreviousPasswords(accountId, RecordStatus.ACTIVE,
                applicationProperties.getPasswordLimitUsage());
        
        if (CommonUtil.isNonNullOrNonEmpty(oldPasswords)) {
            for (String oldPassword : oldPasswords) {
                if (oldPassword.equals(newPassword)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
