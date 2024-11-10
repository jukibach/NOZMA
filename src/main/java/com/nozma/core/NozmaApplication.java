package com.nozma.core;

import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.Role;
import com.nozma.core.entity.account.User;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAspectJAutoProxy
@Slf4j
@AllArgsConstructor
public class NozmaApplication {
    
    @SuppressWarnings("squid:S2479")
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(NozmaApplication.class);
        Environment environment = application.run(args).getEnvironment();
        
        String protocol = Optional.ofNullable(environment.getProperty("server.ssl.key-store"))
                .map(key -> "https")
                .orElse("http");
        
        String applicationName = environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");
        String contextPath = Optional.ofNullable(environment.getProperty("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        
        log.info(
                """
                        
                        --------------------------------------------------
                            Application name    : {}
                            Local               : {}://localhost:{}
                            External            : {}://{}:{}{}
                            Profile(s)          : {}
                        --------------------------------------------------
                        """,
                applicationName, protocol, serverPort, protocol, hostAddress, serverPort, contextPath
                , environment.getActiveProfiles().length == 0
                        ? environment.getDefaultProfiles()
                        : environment.getActiveProfiles());
//
//        Account account1 = new Account(1L, "dungnc23", "123456", "dungnc23@fpt.com",
//                null, null, null, false, Role.builder().build(),
//                User.builder().build());
//
//        Account account2 = new Account(1L, "dungnc23", "123456", "dungnc23@fpt.com",
//                null, null, null, false, Role.builder().build(),
//                User.builder().build());
//
//        HashSet<Account> persons = new HashSet<>();
//        persons.add(account1);
//        persons.add(account2);
//
//        log.info("Is equal ? {}",account2.equals(account1));
//
//        log.info("ToString: {}",account2);
        
//        log.info("Size: {}", persons.size());
//
//        for (Account account: persons) {
//            log.info("Account {}: {}", account.getId(), account.hashCode());
//        }
        
        
//        Map<Account, String> persons = new HashMap<>();
//        persons.put(account1, "Developer");
//
//        log.info("account2: {}", persons.get(account2));
//        log.info("account1: {}", persons.get(account1));
        
    }
}
