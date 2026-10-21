package com.integra.demo.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        Map<String, Object> values = new HashMap<>();
        dotenv.entries().forEach(entry -> values.put(entry.getKey(), entry.getValue()));

        if (!values.isEmpty()) {
            environment.getPropertySources()
                    .addFirst(new MapPropertySource("dotenv", values));
        }
    }
}