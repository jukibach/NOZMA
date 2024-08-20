package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.Role;
import com.ecommerce.userservice.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByIdAndStatus(int id, RecordStatus status);
}
