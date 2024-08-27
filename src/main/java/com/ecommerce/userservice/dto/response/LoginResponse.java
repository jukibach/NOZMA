package com.ecommerce.userservice.dto.response;

import java.util.List;

public record LoginResponse(
        String accountName,
        String email,
        String profileToken,
        String refreshToken,
        String role,
        List<String> privileges,
        String loginTimestamp
) {
}
