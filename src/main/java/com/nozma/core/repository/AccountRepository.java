package com.nozma.core.repository;

import com.nozma.core.entity.account.Account;
import com.nozma.core.projection.AccountView;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    String ACCOUNT_BY_NAME = "accountByName";
    String ACCOUNT_BY_ID = "accountById";
    
    @EntityGraph(attributePaths = {"role", "user"})
    @Cacheable(cacheNames = ACCOUNT_BY_NAME, unless = "#result == null")
    @Transactional(readOnly = true)
    Optional<Account> findOneByAccountName(String accountName);
    
    @EntityGraph(attributePaths = {"role", "user"})
    @Cacheable(cacheNames = ACCOUNT_BY_ID, unless = "#result == null")
    @Transactional(readOnly = true)
    Optional<Account> findOneById(long accountId);
    
    @Query(value = """
        SELECT accounts
        FROM accounts accounts
        WHERE :searchValue IS NULL
        OR accounts.accountName      ILIKE CONCAT('%',:searchValue, '%')
        OR accounts.user.firstName   ILIKE CONCAT('%',:searchValue, '%')
        OR accounts.user.lastName    ILIKE CONCAT('%',:searchValue, '%')
        OR accounts.email            ILIKE CONCAT('%',:searchValue, '%')
    """,
    countQuery = """
        SELECT COUNT(accounts)
        FROM accounts accounts
        WHERE :searchValue IS NULL
                OR accounts.accountName      ILIKE CONCAT('%',:searchValue, '%')
                OR accounts.user.firstName   ILIKE CONCAT('%',:searchValue, '%')
                OR accounts.user.lastName    ILIKE CONCAT('%',:searchValue, '%')
                OR accounts.email            ILIKE CONCAT('%',:searchValue, '%')
    """)
    @Transactional(readOnly = true)
    Page<AccountView> fetchAllByPaging(Pageable pageable, @Param("searchValue") String searchValue);
    
    boolean existsByAccountName(String accountName);
    
    boolean existsByEmail(String email);
    
}
