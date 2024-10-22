package com.nozma.core.repository;

import com.nozma.core.entity.account.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    String ROLE_BY_ID = "roleById";
    
    @Cacheable(cacheNames = ROLE_BY_ID)
    @Transactional(readOnly = true)
    Role findOneById(Integer id);
}
