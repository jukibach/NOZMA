package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.PasswordHistory;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    
    @Query("""
            SELECT ph.password FROM passwordHistories ph
            WHERE ph.accountId = :accountId
            AND ph.status = :status
            ORDER BY ph.id DESC
            LIMIT :limit
            """)
    List<String> findPreviousPasswords(Long accountId, RecordStatus status, int limit);
}
