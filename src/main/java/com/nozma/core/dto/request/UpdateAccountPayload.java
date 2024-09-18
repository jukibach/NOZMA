package com.nozma.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateAccountPayload {
    private String accountName;
    private String email;
    private String firstName;
    private String lastName;
    private String birthdate;
}
