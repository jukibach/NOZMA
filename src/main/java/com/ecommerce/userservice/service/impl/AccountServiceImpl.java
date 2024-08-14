package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.AccountPageResponse;
import com.ecommerce.userservice.dto.response.LoginResponse;
import com.ecommerce.userservice.dto.response.UserRegistrationResponse;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.entity.AccountRole;
import com.ecommerce.userservice.enums.Role;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mapper.AccountMapper;
import com.ecommerce.userservice.mybatis.mapper.MybatisUserMapper;
import com.ecommerce.userservice.repository.AccountRepository;
import com.ecommerce.userservice.repository.AccountRoleRepository;
import com.ecommerce.userservice.repository.RolePrivilegeRepository;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.service.LoginHistoryService;
import com.ecommerce.userservice.service.UserService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.JwtUtil;
import com.ecommerce.userservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
    private final JwtUtil jwtUtil;
    private final MybatisUserMapper mybatisUserMapper;
    private final AuthenticationManager authenticationManager;
    
    @Override
    public Account findByAccountName(String accountName) {
        var account = accountRepository.findByAccountName(accountName);
        if (CommonUtil.isNullOrEmpty(account)) {
            throw new BusinessException("Account ".concat(accountName).concat(" does not exist"));
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
    public LoginResponse login(LoginRequest request) {
        var account = findByAccountName(request.getAccountName());
        
        if (account.isLocked()) {
            loginHistoryService.saveFailedLogin(request, account, "Account is being locked!");
            throw new BusinessException(
                    account.getAccountName()
                            .concat(" has failed login 5 times in a row!")
                            .concat(account.getAccountName())
                            .concat(" has been locked!"));
        }
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            loginHistoryService.saveFailedLogin(request, account, "Incorrect password");
            if (account.isLocked()) {
                throw new BusinessException(account.getAccountName()
                        .concat("'s password is incorrect!")
                        .concat(" Your account is locked"));
            }
            throw new BusinessException(account.getAccountName()
                    .concat("'s password is incorrect!"));
        }
        
        var profileToken = jwtUtil.generateAccessToken(account.getAccountName());
        var accountRoles = accountRoleRepository.findRoleIdAndNameByAccountId(account.getId());
        var roleIds = accountRoles.stream().map(accountRole -> (Integer) accountRole[0]).toList();
        var roleNames = accountRoles.stream().map(accountRole -> (String) accountRole[1]).toList();
        var privilegeNames = rolePrivilegeRepository.findPrivilegeNamesByRoleIds(roleIds);
        
        List<SimpleGrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account, null, authorities));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        loginHistoryService.saveSuccessfulLogin(request, account);
        
        return new LoginResponse(account.getAccountName(), account.getEmail(), profileToken, roleNames, privilegeNames,
                request.getLoginTimestamp());
    }
    
    @Override
    @Transactional
    public UserRegistrationResponse registerNewAccount(UserRegistrationRequest request) {
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
        newAccount = accountRepository.save(newAccount);
        accountRoleRepository.save(new AccountRole(
                new AccountRole.AccountRoleId(newAccount.getId(), Role.USER.getId())));
        var profileToken = jwtUtil.generateAccessToken(request.accountName());
        var privilegeNames = rolePrivilegeRepository.findPrivilegeNamesByRoleId(Role.USER.getId());
        
        // TO-DO:
        // Remember me
        // Allow multiple login
        // Adjust createdBy and updatedBy of the audit log
        // Util
        // Send email
        
        return new UserRegistrationResponse(newAccount.getAccountName(), newAccount.getEmail(), profileToken,
                Collections.singletonList(Role.USER.getRoleName()), privilegeNames);
    }
}
