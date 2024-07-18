package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountName(String accountName);
    
    boolean existsByAccountName(String accountName);
    
    boolean existsByEmail(String email);
    
}
