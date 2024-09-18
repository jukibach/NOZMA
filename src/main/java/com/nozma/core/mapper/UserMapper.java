package com.nozma.core.mapper;

import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.entity.account.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User registrationRequestToAccount(UserRegistrationRequest userRegistrationRequest);
    
}
