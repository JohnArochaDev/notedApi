package com.noted.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DevSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated() // or permitAll() if still testing
                )
                .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
                );

        return http.build();
    }
}
