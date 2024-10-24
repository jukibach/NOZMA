package com.nozma.core.service.impl;

import com.nozma.core.dto.request.EditableAccountPayload;
import com.nozma.core.dto.request.UserRegistrationRequest;
import com.nozma.core.entity.account.User;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.mapper.UserMapper;
import com.nozma.core.repository.UserRepository;
import com.nozma.core.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    
    @Override
    @Transactional
    public User saveNewUser(UserRegistrationRequest request) {
        
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BusinessException("Phone number already exists!");
        }
        
        var newUser = userMapper.registrationRequestToAccount(request);
        newUser = userRepository.save(newUser);
        
        return newUser;
    }
    
    @Override
    @Transactional
    public void updateUser(EditableAccountPayload payload, Long userId) {
        var user = userRepository.findByIdAndStatus(userId, RecordStatus.ACTIVE);
        
        userRepository.save(user);
    }
    
    @Override
    public void deleteUser(long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist!");
        }
        if (RecordStatus.DELETED.equals(user.get().getStatus())) {
            throw new UsernameNotFoundException("User was deleted!");
        }
        user.get().setStatus(RecordStatus.DELETED);
        userRepository.save(user.get());
    }
}
