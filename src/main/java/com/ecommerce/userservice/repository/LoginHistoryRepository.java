package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.LoginHistory;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    
    @Query("""
            SELECT lh.id FROM loginHistories lh WHERE lh.accountName = :accountName
            AND lh.status = :status
            """)
    List<Long> retrieveIdsByAccountNameAndStatus(String accountName, RecordStatus status);
    
    @Modifying
    @Query("""
            UPDATE loginHistories lh SET lh.status = "INACTIVE" WHERE lh.accountName = :accountName
            AND lh.status = "ACTIVE"
            """)
    void deactivateByAccountName(String accountName);
}
