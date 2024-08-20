package com.ecommerce.userservice.dto.request;

import lombok.Data;

@Data
public class ReissueTokenPayload {
    private Long accountId;
    private String refreshToken;
}
