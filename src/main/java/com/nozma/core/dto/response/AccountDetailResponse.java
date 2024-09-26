package com.nozma.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldNameConstants
public class AccountDetailResponse {
    private Long accountId;
    private String accountName;
    private String email;
    private String firstName;
    private String lastName;
    private String creationTime;
    private String lastSignIn;
    private String status;
    private boolean isLocked;
    
}