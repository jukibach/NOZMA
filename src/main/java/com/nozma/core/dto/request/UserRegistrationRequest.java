package com.nozma.core.dto.request;

public record UserRegistrationRequest(
        String accountName,
        String password,
        String confirmedPassword,
        String email,
        String firstName,
        String middleName,
        String lastName,
        String phoneNumber,
        String birthdate
) {
}
