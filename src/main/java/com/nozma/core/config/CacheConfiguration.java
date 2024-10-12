package com.nozma.core.config;

import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.AccountColumn;
import com.nozma.core.entity.account.PasswordHistory;
import com.nozma.core.entity.account.Privilege;
import com.nozma.core.entity.account.Role;
import com.nozma.core.entity.account.RolePrivilege;
import com.nozma.core.entity.account.User;
import com.nozma.core.entity.exercises.ExerciseColumn;
import com.nozma.core.repository.AccountColumnRepository;
import com.nozma.core.repository.AccountRepository;
import com.nozma.core.repository.ExerciseColumnRepository;
import com.nozma.core.repository.RolePrivilegeRepository;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.Objects;

@Configuration
public class CacheConfiguration {
    
    @Bean
    public CacheManager ehCacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();
        
        CacheConfigurationBuilder<Object, Object> cacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Object.class, Object.class, ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(
                                1, MemoryUnit.MB)
                        )
                        .withExpiry(
                                ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofDays(1))
                        );
        
        javax.cache.configuration.Configuration<Object, Object> configuration =
                Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
        
        createCache(cacheManager, AccountRepository.ACCOUNT_BY_NAME, configuration);
        createCache(cacheManager, AccountRepository.ACCOUNT_BY_ID, configuration);
        
        createCache(cacheManager, AccountColumnRepository.ALL_ACCOUNT_COLUMN, configuration);
        
        createCache(cacheManager, RolePrivilegeRepository.PRIVILEGES_BY_ROLE_ID, configuration);
        createCache(cacheManager, ExerciseColumnRepository.EXERCISE_COLUMN_VIEW_BY_STATUS, configuration);
        
        createCache(cacheManager, Account.class.getName(), configuration);
        createCache(cacheManager, Account.class.getName() + ".role", configuration);
        createCache(cacheManager, Account.class.getName() + ".user", configuration);
        createCache(cacheManager, User.class.getName(), configuration);
        createCache(cacheManager, Role.class.getName(), configuration);
        createCache(cacheManager, Privilege.class.getName(), configuration);
        createCache(cacheManager, PasswordHistory.class.getName(), configuration);
        createCache(cacheManager, RolePrivilege.class.getName(), configuration);
        createCache(cacheManager, ExerciseColumn.class.getName(), configuration);
        createCache(cacheManager, AccountColumn.class.getName(), configuration);
        
        return cacheManager;
    }
    
    private void createCache(
            CacheManager cm,
            String cacheName,
            javax.cache.configuration.Configuration<Object, Object> configuration
    ) {
        Cache<Object, Object> cache = cm.getCache(cacheName);
        if (Objects.nonNull(cache)) {
            cache.clear();
        }
        else {
            cm.createCache(cacheName, configuration);
        }
    }
}
