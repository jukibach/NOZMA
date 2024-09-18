package com.nozma.core.config;

import com.nozma.core.entity.account.JwtAccountDetails;
import com.nozma.core.util.CommonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (CommonUtil.isNullOrEmpty(authentication) || !authentication.isAuthenticated()
                    || "anonymousUser".equals(authentication.getName())) {
                return Optional.of("system");
            }
            JwtAccountDetails account = (JwtAccountDetails) authentication.getPrincipal();
            return Optional.of(account.getAccountName());
        };
    }
}
