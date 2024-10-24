package com.nozma.core.service;

import com.nozma.core.dto.request.EditableAccountPayload;
import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.entity.account.User;

public interface UserService {
    User saveNewUser(UserRegistrationRequest request);
    
    void updateUser(EditableAccountPayload payload, Long userId);
    
    void deleteUser(long userId);
}
