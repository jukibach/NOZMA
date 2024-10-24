package com.nozma.core.dto.request;

import com.nozma.core.annotations.CustomNotNull;
import com.nozma.core.constant.FieldNamesConstant;
import com.nozma.core.constant.MessageConstant;
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
public class EditableAccountPayload {
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.ACCOUNT_NAME)
    private String accountName;
    
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.EMAIL)
    private String email;
    
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.FIRST_NAME)
    private String firstName;
    
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.LAST_NAME)
    private String lastName;
    
    @CustomNotNull(message = MessageConstant.FIELD_REQUIRED, fieldCode = FieldNamesConstant.BIRTHDATE)
    private String birthdate;
}
