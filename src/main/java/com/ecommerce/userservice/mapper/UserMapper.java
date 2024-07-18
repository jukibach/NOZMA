package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User registrationRequestToAccount(UserRegistrationRequest userRegistrationRequest);
    
}
