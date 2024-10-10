package com.nozma.core.dto.response;

public record AccountDetailResponse(
        Long accountId,
        String accountName,
        String email,
        String fullName,
        String creationTime,
        String lastModified,
        String status,
        boolean isLocked
) {
}