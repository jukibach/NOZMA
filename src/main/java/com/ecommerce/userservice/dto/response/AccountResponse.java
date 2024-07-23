package com.ecommerce.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private String accountName;
    private String email;
    private boolean isLocked;
    private String creationTime;
    private String lastSignIn;
    private Integer groups;
    private String passwordAge;
}
