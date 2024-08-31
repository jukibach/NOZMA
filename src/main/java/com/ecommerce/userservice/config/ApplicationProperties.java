package com.ecommerce.userservice.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class ApplicationProperties {
    
    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;
    
    @Value("${server.address}")
    private String address;
    
    @Value("${server.port}")
    private int port;
    
    @Value("${authentication.profile-token.timeout}")
    private int profileTokenTimeout;
    
    @Value("${authentication.refresh-token.timeout}")
    private int refreshTokenTimeout;
    
    @Value("${authentication.max-failed-attempts}")
    private int maxFailedAttempts;
    
    @Value("${authentication.lock-time-in-minute}")
    private int lockTimeInMinute;
    
    @Value("${authentication.password-duration-in-days}")
    private int passwordDurationInDays;
    
    @Value("${authentication.password-limit-usage}")
    private int passwordLimitUsage;
    
    @Value("${authentication.password-generated-length}")
    private int passwordGeneratedLength;
    
    @Value("${authentication.password.generate.number-of-lower-case-character}")
    private int numberOfLowerCaseCharacter;
    
    @Value("${authentication.password.generate.number-of-upper-case-character}")
    private int numberOfUpperCaseCharacter;
    
    @Value("${authentication.password.generate.number-of-digit-character}")
    private int numberOfDigitCharacter;
    
    @Value("${authentication.password.generate.number-of-special-character}")
    private int numberOfSpecialCharacter;
    
    @Value("${authentication.password-special-characters}")
    private String specialCharacters;
    
    @Value("${authentication.password-day-left-to-warning}")
    private int passwordDayLeftToWarning;
    
    @Value("${spring.aws.access-key}")
    private String accessKey;
    
    @Value("${spring.aws.secret-key}")
    private String secretKey;
    
    @Value("${key.local-path}")
    private String keyLocalPath;
    
    @Value("${key.jwt-sign-private-key}")
    private String jwtSignPrivateKey; // TODO: what is a der file ?
    
    
    @Value("${key.jwt-sign-public-key}")
    private String jwtSignPublicKey; // TODO: what is a der file ?
    
    @Value("${key.aws-kms-key-id}")
    private String kmsKeyId;
    
    @Value("${cors.allowCredentials}")
    private boolean corsAllowCredentials;
    
    @Value("${cors.allowOrigins}")
    private List<String> corsAllowOrigins;
    
    @Value("${cors.allowHeaders}")
    private List<String> corsAllowHeaders;
    
    @Value("${cors.allowExposedHeaders}")
    private List<String> corsAllowExposedHeaders;
    
    @Value("${cors.allowMethods}")
    private List<String> corsAllowMethods;
    
}
