package com.nozma.core.dto.response;

public record AccountViewResponse(
        Long accountId,
        String accountName,
        String email,
        String fullName,
        String birthdate,
        String creationTime,
        String lastModified,
        String status
) {
}