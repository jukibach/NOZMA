package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    
    @Query("""
            SELECT ar.roleId, ar.roleName
            FROM AccountRoles ar
            WHERE ar.accountId = :accountId
            """)
    List<Object[]> findRoleIdAndNameByAccountId(Long accountId);
}
