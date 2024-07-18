package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.entity.User;

public interface UserService {
    User saveNewUser(UserRegistrationRequest request);
}
