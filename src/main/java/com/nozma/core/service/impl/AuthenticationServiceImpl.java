package com.nozma.core.service.impl;

import com.nozma.core.config.ApplicationProperties;
import com.nozma.core.dto.request.ChangePasswordPayload;
import com.nozma.core.dto.request.LoginRequest;
import com.nozma.core.dto.request.ReissueTokenPayload;
import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.dto.response.LoginResponse;
import com.nozma.core.dto.response.ReissueTokenResponse;
import com.nozma.core.dto.response.UserRegistrationResponse;
import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.entity.account.PasswordHistory;
import com.nozma.core.entity.account.TokenDetail;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.Role;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.enums.TokenType;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.mapper.AccountMapper;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.repository.PasswordHistoryRepository;
import com.nozma.core.repository.RolePrivilegeRepository;
import com.nozma.core.repository.RoleRepository;
import com.nozma.core.service.AccountService;
import com.nozma.core.service.AuthenticationService;
import com.nozma.core.service.LoginHistoryService;
import com.nozma.core.service.TokenService;
import com.nozma.core.service.UserService;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.DateUtil;
import com.nozma.core.util.PasswordGeneratorUtil;
import com.nozma.core.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
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
import java.util.Collections;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final LoginHistoryService loginHistoryService;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordGeneratorUtil passwordGeneratorUtil;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ApplicationProperties applicationProperties;
    private final RoleRepository roleRepository;
    
    @Override
    public LoginResponse login(LoginRequest request)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getAccountName(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            JwtAccountDetails accountDetails = (JwtAccountDetails) authentication.getPrincipal();
            var account = accountDetails.getAccount();
            
            loginHistoryService.resetFailedAttempts(account.getAccountName());
            
            var profileToken = tokenService.generateToken(accountDetails, TokenType.PROFILE_TOKEN);
            var refreshToken = tokenService.generateToken(accountDetails, TokenType.REFRESH_TOKEN);
            
            return new LoginResponse(account.getId(), account.getAccountName(), account.getEmail(), profileToken, refreshToken,
                    accountDetails.getUserRole(), accountDetails.getPrivileges());
            
        } catch (Exception exception) {
            var account = accountRepository.findOneByAccountName(request.getAccountName());
            
            if (account.isPresent()) {
                if (exception instanceof BadCredentialsException) {
                    loginHistoryService.lockWhenMultipleFailedAttempts(request, account.get());
                }
                else if (exception instanceof LockedException) {
                    loginHistoryService.unlockWhenExpired(account.get());
                }
            }
            throw exception;
        }
    }
    
    @Override
    @Transactional
    public UserRegistrationResponse registerNewAccount(UserRegistrationRequest request)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (!Objects.equals(request.password(), request.confirmedPassword())) {
            log.error("Password is incorrect");
            throw new BusinessException(StatusAndMessage.INCORRECT_PASSWORD);
        }
        
        if (accountRepository.existsByAccountName(request.accountName())) {
            log.error("Account already existed");
            throw new BusinessException(StatusAndMessage.ACCOUNT_ALREADY_EXISTED);
        }
        
        if (accountRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already exists!");
        }
        
        var newUser = userService.saveNewUser(request);
        var newAccount = accountMapper.registrationRequestToAccount(request);
        var fromDate = DateUtil.getCurrentDate();
        var toDate = DateUtil.plusDay(fromDate, applicationProperties.getPasswordDurationInDays());
        var encodedPassword = passwordEncoder.encode(request.password());
        newAccount.setPassword(encodedPassword);
        newAccount.setUser(newUser);
        newAccount.setFromDate(fromDate);
        newAccount.setToDate(toDate);
        newAccount.setRole(roleRepository.findById(Role.USER.getId()).get());
        newAccount = accountRepository.save(newAccount);
        
        var accountDetails = JwtAccountDetails.builder()
                .account(Account.builder()
                        .id(newAccount.getId())
                        .accountName(newAccount.getAccountName())
                        .build())
                .build();
        var profileToken = tokenService.generateToken(accountDetails, TokenType.PROFILE_TOKEN);
        var refreshToken = tokenService.generateToken(accountDetails, TokenType.REFRESH_TOKEN);
        
        var privilegeNames = rolePrivilegeRepository.findPrivilegeNamesByRoleId(Role.USER.getId());
        
        PasswordHistory passwordHistory = PasswordHistory.builder()
                .password(encodedPassword)
                .fromDate(fromDate)
                .toDate(toDate)
                .accountId(newAccount.getId())
                .build();
        passwordHistoryRepository.save(passwordHistory);
        
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
        var account = accountService.findByAccountName(accountName);
        
        if (account.isLocked()) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_LOCKED_AFTER_5_FAILED_ATTEMPTS);
        }
        if (RecordStatus.INACTIVE.equals(account.getStatus())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_HAS_BEEN_DELETED);
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
            throw new BusinessException("Account is invalid");
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
        var currentAccount = accountService.findByAccountName(SecurityUtil.getCurrentAccountName());
        
        checkChangePassword(payload, currentAccount);
        
        String encryptedPassword = passwordEncoder.encode(payload.getNewPassword());
        
        updatePasswordInTheDatabase(currentAccount, encryptedPassword, Boolean.FALSE);
        
    }
    
    // TODO: What if I register -> create a new history. I change password again ?
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
