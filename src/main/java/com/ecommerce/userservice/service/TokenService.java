package com.ecommerce.userservice.service;

import com.ecommerce.userservice.entity.JwtAccountDetails;
import com.ecommerce.userservice.entity.TokenDetail;
import com.ecommerce.userservice.enums.TokenType;
import com.ecommerce.userservice.exception.BusinessException;

public interface TokenService {
    String generateToken(JwtAccountDetails jwtAccountDetails, TokenType tokenType) throws BusinessException;
    
    TokenDetail validateToken(String token) throws BusinessException;
    
    void addTokenToBlackList(String token);
    
    boolean checkTokenExistsInBlackList(String token);
}
