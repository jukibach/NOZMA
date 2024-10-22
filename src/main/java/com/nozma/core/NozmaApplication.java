package com.nozma.core;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    }
}
