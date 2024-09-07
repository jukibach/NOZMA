package com.ecommerce.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class UserServiceApplication {
    
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(UserServiceApplication.class);
        Environment environment = application.run(args).getEnvironment();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n-------------------------\n");
        stringBuilder.append(String.format("\t%-20s: {}%n", "Application name"));
        stringBuilder.append(String.format("\t%-20s: http://localhost:{}%n", "Local"));
        stringBuilder.append(String.format("\t%-20s: http://{}:{}", "External"));
        stringBuilder.append("\n-------------------------\n");

        try {
            log.info(stringBuilder.toString(),
                    environment.getProperty("spring.application.name"),
                    environment.getProperty("server.port"),
                    InetAddress.getLocalHost().getHostAddress(),
                    environment.getProperty("server.port"));
        } catch (UnknownHostException exception) {
            log.info(stringBuilder.toString(), environment.getProperty("spring.application.name"),
                    environment.getProperty("server.port"), "[N/A]", environment.getProperty("server.port"));
            log.error("Error when retrieving external IP: ", exception);
        }
    }
    
//    @EventListener(ApplicationReadyEvent.class)
//    public void contextRefreshedEvent() {
//
//    }
}
