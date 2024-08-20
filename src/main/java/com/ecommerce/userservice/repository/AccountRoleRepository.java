package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    
    @Query("""
            SELECT ar.role.id, r.name
            FROM AccountRoles ar
            INNER JOIN Roles r
            ON ar.role.id = r.id
            WHERE ar.account.id = :accountId
            """)
    List<Object[]> findRoleIdAndNameByAccountId(Long accountId);
}
