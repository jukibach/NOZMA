package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, RolePrivilege.RolePrivilegeId> {
    
    @Query("""
            SELECT p.name
            FROM RolePrivileges rp
            INNER JOIN Privileges p
            ON rp.id.roleId = p.id
            WHERE rp.id.roleId IN :roleIds
            GROUP BY p.name
            """)
    List<String> findPrivilegeNamesByRoleIds(List<Integer> roleIds);
    
    @Query("""
            SELECT p.name
            FROM RolePrivileges rp
            INNER JOIN Privileges p
            ON rp.id.roleId = p.id
            WHERE rp.id.roleId = :roleId
            GROUP BY p.name
            """)
    List<String> findPrivilegeNamesByRoleId(Integer roleId);
}
