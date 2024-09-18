package com.nozma.core.mapper;

import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.entity.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    
    @Mapping(target = "password", ignore = true)
    Account registrationRequestToAccount(UserRegistrationRequest userRegistrationRequest);
    
}
