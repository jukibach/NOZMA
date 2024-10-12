package com.nozma.core.repository;

import com.nozma.core.entity.account.AccountColumn;
import com.nozma.core.enums.RecordStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountColumnRepository extends JpaRepository<AccountColumn, Long> {
    String ALL_ACCOUNT_COLUMN = "allAccountColumn";
    
    @Cacheable(cacheNames = ALL_ACCOUNT_COLUMN)
    @Transactional(readOnly = true)
    List<AccountColumn> findAllByStatus(RecordStatus status);
    
}
