package com.backend.demo.config;


import feign.Logger;
import org.springframework.context.annotation.Bean;


public class FeignLoggingConfiguration {
    @Bean
    public Logger.Level feignLoggerLeve() {
        return Logger.Level.FULL;
    }
}
