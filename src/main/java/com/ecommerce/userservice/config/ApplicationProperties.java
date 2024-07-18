package com.ecommerce.userservice.config;

import com.ecommerce.userservice.constant.CommonConstant;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {
    
    Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    @Value("${server.address}")
    private String address;
    
    @Value("${server.port}")
    private int port;
    
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;
    
    @EventListener(ApplicationReadyEvent.class)
    public void contextRefreshedEvent() {
        String serverUrl = "Server running at: https://" + this.address + CommonConstant.COLON + this.port;
        logger.info(serverUrl);
    }
}
