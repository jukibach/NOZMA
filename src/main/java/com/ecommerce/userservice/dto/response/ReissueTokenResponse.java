package com.ecommerce.userservice.dto.response;

public record ReissueTokenResponse(
        String profileToken,
        String refreshToken
) {
}
