package com.ecommerce.userservice.util;

import com.ecommerce.userservice.config.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
@Component
public class JwtUtil {
    
    private final ApplicationProperties applicationProperties;
    
    // https://dev.to/m1guelsb/authentication-and-authorization-with-spring-boot-4m2n
    
    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
    
    
}
