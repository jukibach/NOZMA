package com.ecommerce.userservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.ecommerce.userservice.cache.CacheStore;
import com.ecommerce.userservice.config.ApplicationProperties;
import com.ecommerce.userservice.constant.Constant;
import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.entity.TokenDetail;
import com.ecommerce.userservice.enums.TokenType;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.service.TokenService;
import com.ecommerce.userservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAKey;
import java.util.Calendar;

@AllArgsConstructor
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
    private final CacheStore<String, String> blackListCache;
    private final ApplicationProperties applicationProperties;
    private final RSAKeyProvider keyProvider; // using AWS
    @Override
    public String generateToken(JwtAccountDetails jwtAccountDetails, TokenType tokenType) throws BusinessException {
        log.info("Generating {}", tokenType);
        var currentDate = Calendar.getInstance();
        currentDate.add(Calendar.SECOND, -30);
        var expiryDate = Calendar.getInstance();
        
        if (TokenType.PROFILE_TOKEN.equals(tokenType)) {
            expiryDate.add(Calendar.MINUTE, applicationProperties.getProfileTokenTimeout());
        }
        else {
            expiryDate.add(Calendar.MINUTE, applicationProperties.getRefreshTokenTimeout());
        }
        
        var algorithm = Algorithm.RSA256(null, keyProvider.getPrivateKey());
        
        return JWT.create().withSubject(jwtAccountDetails.getUsername())
                .withClaim("accountId", jwtAccountDetails.getAccount().getId())
                .withIssuedAt(currentDate.getTime())
                .withExpiresAt(expiryDate.getTime())
                .sign(algorithm);
    }
    
    @Override
    public TokenDetail validateToken(String token) throws BusinessException {
        var algorithm = Algorithm.RSA256(keyProvider.getPublicKeyById(Constant.EMPTY), null);
        JWTVerifier verifier = JWT.require(algorithm).build();
        
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            
            return TokenDetail.builder()
                    .accountId(decodedJWT.getClaim("accountId").asLong())
                    .expiryDate(decodedJWT.getExpiresAt())
                    .issueDate(decodedJWT.getIssuedAt())
                    .accountName(decodedJWT.getSubject())
                    .build();
        } catch (Exception exception) {
            log.error("Invalid token: ", exception);
            throw new BusinessException("Invalid");
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
