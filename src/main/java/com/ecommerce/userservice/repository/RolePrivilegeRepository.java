package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    
    @Query("""
            SELECT rp.privilegeName
            FROM RolePrivileges rp
            WHERE rp.roleId = :roleId
            GROUP BY rp.privilegeName
            """)
    List<String> findPrivilegeNamesByRoleId(Integer roleId);
}
