package com.ecommerce.userservice.service;

import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.entity.TokenDetail;
import com.ecommerce.userservice.enums.TokenType;
import com.ecommerce.userservice.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface TokenService {
    String generateToken(JwtAccountDetails jwtAccountDetails, TokenType tokenType) throws BusinessException, IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    
    TokenDetail validateToken(String token) throws BusinessException, NoSuchAlgorithmException, InvalidKeySpecException, IOException;
    
    void addTokenToBlackList(String token);
    
    boolean checkTokenExistsInBlackList(String token);
}
