package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, AccountRole.AccountRoleId> {
    
    @Query("""
            SELECT ar.id.roleId, r.name
            FROM AccountRoles ar
            INNER JOIN Roles r
            ON ar.id.roleId = r.id
            WHERE ar.id.accountId = :accountId
            """)
    List<Object[]> findRoleIdAndNameByAccountId(Long accountId);
}
