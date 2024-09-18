package com.nozma.core.repository;

import com.nozma.core.entity.account.User;
import com.nozma.core.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    
    User findByIdAndStatus(Long userId, RecordStatus status);
}
