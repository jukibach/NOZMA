package com.nozma.core.repository;

import com.nozma.core.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountName(String accountName);
    
    boolean existsByAccountName(String accountName);
    
    boolean existsByEmail(String email);
    
}
