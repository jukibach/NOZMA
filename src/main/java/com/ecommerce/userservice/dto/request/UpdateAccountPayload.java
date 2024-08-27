package com.ecommerce.userservice.dto.request;

import com.ecommerce.userservice.annotations.CustomNotNull;
import com.ecommerce.userservice.constant.FieldNamesConstant;
import com.ecommerce.userservice.constant.MessageConstant;
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
