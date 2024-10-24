package com.nozma.core.dto.response;

import java.util.List;

public record AccountDetailResponse(
        Long accountId,
        String accountName,
        String email,
        String firstName,
        String lastName,
        String birthdate,
        String creationTime,
        String lastModified,
        String status,
        List<AccountColumnResponse> columns
) {
}