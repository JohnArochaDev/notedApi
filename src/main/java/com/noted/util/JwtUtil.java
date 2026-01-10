package com.noted.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecretString;

    private SecretKey secretKey;

    private final long expirationMs = 1000 * 60 * 60 * 24; // 24 hours

    @PostConstruct
    public void init() {
        if (jwtSecretString == null || jwtSecretString.trim().isEmpty()) {
            throw new IllegalStateException(
                "JWT secret is missing or empty! Please set 'jwt.secret' in application properties " +
                "or environment variable JWT_SECRET"
            );
        }

        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);

        // Safety check - very important
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                "JWT secret is too short! Minimum 32 bytes required. " +
                "Current length: " + keyBytes.length + " bytes. " +
                "Use at least: openssl rand -base64 48"
            );
        }

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UUID userId, String username) {
        return Jwts.builder()
                .claim("userId", userId.toString())
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaims(token).get("userId", String.class));
    }

    public String extractUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}