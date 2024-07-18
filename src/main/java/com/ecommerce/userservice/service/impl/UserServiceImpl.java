package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.request.UserRegistrationRequest;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    
    @Override
    public User saveNewUser(UserRegistrationRequest request) {
        
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BusinessException("Phone number already exists!");
        }
        
        var newUser = userMapper.registrationRequestToAccount(request);
        newUser = userRepository.save(newUser);
        
        return newUser;
    }
}
