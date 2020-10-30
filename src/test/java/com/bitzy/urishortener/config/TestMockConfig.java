package com.bitzy.urishortener.config;

import com.bitzy.urishortener.service.KeyGeneratorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class TestMockConfig {

    @Bean
    @Primary
    public KeyGeneratorService mockKeyGeneratorService() {
        return new KeyGeneratorService(){

            @Override
            public String generateKey(String uri) {
                return "ABC123DEF456";
            }
            
        };
    }
}