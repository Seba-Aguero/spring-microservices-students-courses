package com.microservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv(ConfigurableEnvironment environment) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        Map<String, Object> envVars = new HashMap<>();
        dotenv.entries().forEach(e -> envVars.put(e.getKey(), e.getValue()));

        environment.getPropertySources().addFirst(new MapPropertySource("dotenvVars", envVars));

        return dotenv;
    }
}