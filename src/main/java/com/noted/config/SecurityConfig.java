package com.noted.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF — JWT is stateless, no need for CSRF protection
                .csrf(csrf -> csrf.disable())
                // Make session stateless — we don't use server-side sessions
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Public endpoints — no authentication required
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/noted/users/register",
                        "/noted/users/login"
                ).permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated() // All other endpoints require valid JWT
                )
                // Allow frames for H2 console
                .headers(headers
                        -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                // Add JWT filter before Spring's default authentication filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
