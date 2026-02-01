package com.allo.restaurant.menu.annotations;

import com.allo.restaurant.menu.ports.outbound.TimeProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;

@TestConfiguration
public class FixedDataConfiguration {

    @Bean
    @Primary
    public TimeProvider timeProvider() {
        return () -> LocalDateTime.of(2026, 1, 1, 0, 0);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
