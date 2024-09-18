package com.nozma.core.dto.response;

import java.util.List;

public record UserRegistrationResponse(
        String accountName,
        String email,
        String refreshToken,
        String profileToken,
        List<String> roles,
        List<String> privileges
) {
}
