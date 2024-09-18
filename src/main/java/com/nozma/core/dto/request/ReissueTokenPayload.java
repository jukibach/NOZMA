package com.nozma.core.dto.request;

import lombok.Data;

@Data
public class ReissueTokenPayload {
    private Long accountId;
    private String refreshToken;
}
