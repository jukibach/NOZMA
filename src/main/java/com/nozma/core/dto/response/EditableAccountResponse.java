package com.nozma.core.dto.response;

public record EditableAccountResponse(
        Long accountId,
        String accountName,
        String email,
        String firstName,
        String lastName,
        String birthdate,
        String creationTime,
        String lastModified,
        String status,
        String isLocked
) {
}