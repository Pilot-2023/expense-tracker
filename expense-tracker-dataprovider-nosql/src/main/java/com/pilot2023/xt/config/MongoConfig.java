package com.pilot2023.xt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@Configuration
@EnableMongoRepositories
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new CurrentUserAuditorAware();
    }

    static class CurrentUserAuditorAware implements AuditorAware<String> {

        // Implement this method to return the current user's name or ID
        @Override
        public Optional<String> getCurrentAuditor() {
            // You can retrieve the current user from your security context or any other source.
            // For example:
            // return Optional.of("current_username");
            return Optional.empty(); // If you don't have a user context, return empty.
        }

    }

}