package com.nozma.core.service;

import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.entity.account.TokenDetail;
import com.nozma.core.enums.TokenType;
import com.nozma.core.exception.BusinessException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface TokenService {
    String generateToken(JwtAccountDetails jwtAccountDetails, TokenType tokenType) throws BusinessException, IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    TokenDetail validateToken(String token) throws BusinessException, NoSuchAlgorithmException, InvalidKeySpecException, IOException;
    
    void addTokenToBlackList(String token);
    
    boolean checkTokenExistsInBlackList(String token);
}
