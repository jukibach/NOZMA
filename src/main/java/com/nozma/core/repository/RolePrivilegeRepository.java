package com.nozma.core.repository;

import com.nozma.core.entity.account.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    
    @Query("""
            SELECT p.name
            FROM RolePrivileges rp
            INNER JOIN Roles r ON r.id = rp.roleId
            INNER JOIN Privileges p ON p.id = rp.privilegeId
            WHERE rp.roleId = :roleId
            GROUP BY p.name
            """)
    List<String> findPrivilegeNamesByRoleId(Integer roleId);
}
