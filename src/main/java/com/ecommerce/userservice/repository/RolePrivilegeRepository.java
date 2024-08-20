package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    
    @Query("""
            SELECT p.name
            FROM RolePrivileges rp
            INNER JOIN Privileges p
            ON rp.role.id = p.id
            WHERE rp.role.id IN :roleIds
            GROUP BY p.name
            """)
    List<String> findPrivilegeNamesByRoleIds(List<Integer> roleIds);
    
    @Query("""
            SELECT p.name
            FROM RolePrivileges rp
            INNER JOIN Privileges p
            ON rp.role.id = p.id
            WHERE rp.role.id = :roleId
            GROUP BY p.name
            """)
    List<String> findPrivilegeNamesByRoleId(Integer roleId);
}
