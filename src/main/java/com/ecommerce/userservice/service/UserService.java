package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.UpdateAccountPayload;
import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.entity.User;

public interface UserService {
    User saveNewUser(UserRegistrationRequest request);
    
    void updateUser(UpdateAccountPayload payload, Long userId);
    
    void deleteUser(long userId);
}
