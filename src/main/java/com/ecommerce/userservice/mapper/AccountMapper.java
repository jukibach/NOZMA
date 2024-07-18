package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.dto.response.LoginResponse;
import com.ecommerce.userservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    LoginResponse accountToLoginResponse(Account account);
    
    @Mapping(target = "password", ignore = true)
    Account registrationRequestToAccount(UserRegistrationRequest userRegistrationRequest);
    
}
