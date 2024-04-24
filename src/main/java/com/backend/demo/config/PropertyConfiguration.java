package com.backend.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@SuppressWarnings("unused")
@Slf4j
public class PropertyConfiguration {

    @Profile("test")
    @Bean
    public String testDatabaseConnection(){
        final String message = "Datasource for TEST environment: PSQL";
        log.info(message);
        return message;
    }
    @Profile("dev")
    @Bean
    public String devDatabaseConnection(){
        final String message = "Datasource for DEV environment: PSQL";
        log.info(message);
        return message;
    }

    @Profile("prod")
    @Bean
    public String prodDatabaseConnection(){
        final String message = "Datasource for PROD environment: PSQL";
        log.info(message);
        return message;
    }
}
