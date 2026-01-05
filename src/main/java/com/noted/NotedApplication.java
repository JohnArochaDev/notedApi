package com.noted;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class NotedApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotedApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDataSource(JdbcTemplate jdbcTemplate) {
        return args -> jdbcTemplate.execute("SELECT 1");
    }
}
