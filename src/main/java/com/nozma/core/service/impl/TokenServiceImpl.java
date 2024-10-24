package com.nozma.core.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nozma.core.cache.CacheStore;
import com.nozma.core.config.ApplicationProperties;
import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.entity.account.TokenDetail;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.enums.TokenType;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.service.TokenService;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.KeyProviderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

@AllArgsConstructor
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
    private final CacheStore<String, String> blackListCache;
    private final ApplicationProperties applicationProperties;
    private final KeyProviderUtil keyProviderUtil; // using AWS
    
    @Override
    public String generateToken(JwtAccountDetails jwtAccountDetails, TokenType tokenType)
            throws BusinessException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Generating {}", tokenType);
        var currentDate = Calendar.getInstance();
        currentDate.add(Calendar.SECOND, -30); // Delay between steps
        var expiryDate = Calendar.getInstance();
        
        if (TokenType.PROFILE_TOKEN.equals(tokenType)) {
            expiryDate.add(Calendar.MINUTE, applicationProperties.getProfileTokenTimeout());
        } else {
            expiryDate.add(Calendar.MINUTE, applicationProperties.getRefreshTokenTimeout());
        }
        
        JWTCreator.Builder jwt = JWT.create()
                .withSubject(String.valueOf(jwtAccountDetails.getAccount().getId()))
                .withClaim(Account.Fields.accountName, jwtAccountDetails.getAccount().getAccountName())
                .withIssuedAt(currentDate.getTime())
                .withExpiresAt(expiryDate.getTime());
        
        return keyProviderUtil.generate(jwt);
    }
    
    @Override
    public TokenDetail validateToken(String token) throws BusinessException, IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        var algorithm = Algorithm.RSA256(keyProviderUtil.getPublicKey(), null);
        JWTVerifier verifier = JWT.require(algorithm).build();
        
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            
            return TokenDetail.builder()
                    .accountId(Long.valueOf(decodedJWT.getSubject()))
                    .expiryDate(decodedJWT.getExpiresAt())
                    .issueDate(decodedJWT.getIssuedAt())
                    .accountName(decodedJWT.getClaim(Account.Fields.accountName).asString())
                    .build();
        } catch (TokenExpiredException exception) {
            throw new BusinessException(StatusAndMessage.TOKEN_EXPIRED);
        } catch (JWTDecodeException exception) {
            throw new JWTVerificationException("JWT is malformed");
        } catch (SignatureVerificationException exception) {
            throw new JWTVerificationException("Signature does not match");
        } catch (AlgorithmMismatchException exception) {
            throw new JWTVerificationException("Algorithm does not match");
        } catch (InvalidClaimException exception) {
            throw new JWTVerificationException("Invalid claim");
        } catch (Exception exception) {
            throw new JWTVerificationException(exception.getMessage());
        }
    }
    
    @Override
    public void addTokenToBlackList(String token) {
        blackListCache.add(token, token);
    }
    
    @Override
    public boolean checkTokenExistsInBlackList(String token) {
        String existToken = blackListCache.get(token);
        return CommonUtil.isNonNullOrNonEmpty(existToken);
    }
}
