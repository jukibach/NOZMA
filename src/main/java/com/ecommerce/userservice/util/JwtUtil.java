package com.ecommerce.userservice.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ecommerce.userservice.config.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@AllArgsConstructor
@Component
public class JwtUtil {
    
    private final ApplicationProperties applicationProperties;
    
    // https://dev.to/m1guelsb/authentication-and-authorization-with-spring-boot-4m2n
    public String generateAccessToken(String accountName) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(applicationProperties.getJwtSecretKey());
            return JWT.create()
                    .withSubject(accountName)
                    .withClaim("accountName", accountName)
                    .withExpiresAt(Date.from(genAccessExpirationDate()))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }
    
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(applicationProperties.getJwtSecretKey());
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }
    
    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
