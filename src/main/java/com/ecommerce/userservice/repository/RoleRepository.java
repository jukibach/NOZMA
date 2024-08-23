package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    @Query(value = """
                SELECT ar.roleId, ar.roleName
                FROM AccountRoles ar
                WHERE ar.accountId = :accountId
            """)
    List<Object[]> findRoleNamesByAccountId(Long accountId);
}
