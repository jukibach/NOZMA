package com.ecommerce.userservice.config;

import com.ecommerce.userservice.constant.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class ApplicationProperties {
    
    @Value("${server.address}")
    private String address;
    
    @Value("${server.port}")
    private int port;
    
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;
    
    @Value("${authentication.profile-token.timeout}")
    private Integer profileTokenTimeout;
    
    @Value("${authentication.refresh-token.timeout}")
    private Integer refreshTokenTimeout;
}
