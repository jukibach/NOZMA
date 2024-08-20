package com.ecommerce.userservice.dto.response;

import java.util.List;

public record LoginResponse(
        String accountName,
        String email,
        String profileToken,
        String refreshToken,
        List<String> roles,
        List<String> privileges,
        String loginTimestamp
) {
}
