package com.nozma.core.config;

import com.nozma.core.constant.Constant;
import com.nozma.core.util.SecurityUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware  implements AuditorAware<String> {
    
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtil.getCurrentAccountName().orElse(Constant.SYSTEM_AUDITOR));
    }
}
