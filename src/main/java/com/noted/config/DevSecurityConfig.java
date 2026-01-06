package com.noted.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DevSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/noted/users/register", "/noted/users/login", "/noted/users/change-password", "/noted/users/delete-account").permitAll() // Allow these public endpoints without auth
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/noted/users/**", "/h2-console/**")
                )
                .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }
}
