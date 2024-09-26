package com.nozma.core.service;

import com.nozma.core.dto.request.UpdateAccountPayload;
import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.entity.account.User;

public interface UserService {
    User saveNewUser(UserRegistrationRequest request);
    
    void updateUser(UpdateAccountPayload payload, Long userId);
    
    void deleteUser(long userId);
}
