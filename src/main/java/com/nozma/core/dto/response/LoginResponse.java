package com.nozma.core.dto.response;

import java.util.List;

public record LoginResponse(
        Long accountId,
        String accountName,
        String email,
        String profileToken,
        String refreshToken,
        String role,
        List<String> privileges
) {
}
