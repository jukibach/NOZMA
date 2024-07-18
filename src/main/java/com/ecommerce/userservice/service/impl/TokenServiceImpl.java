package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.cache.CacheStore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl {
    private final CacheStore<String, String> token;

    
}
