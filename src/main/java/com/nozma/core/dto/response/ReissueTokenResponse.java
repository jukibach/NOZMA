package com.nozma.core.dto.response;

public record ReissueTokenResponse(
        String profileToken,
        String refreshToken
) {
}
