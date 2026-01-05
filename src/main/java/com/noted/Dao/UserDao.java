package com.noted.Dao;

import java.util.Map;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.noted.models.User;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserDao(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String INSERT_USER
            = "INSERT INTO users (user_id, username, password) VALUES (?, ?, ?)";

    private static final String DELETE_USER
            = "DELETE FROM users WHERE user_id = ?";

    private static final String FIND_BY_USERNAME
            = "SELECT user_id, username, password FROM users WHERE username = ?";

    private static final String USERNAME_EXISTS
            = "SELECT COUNT(*) FROM users WHERE username = ?";

    public void insertUser(UUID userId, String username, String hashedPassword) {
        jdbcTemplate.update(INSERT_USER, userId, username, hashedPassword);
    }

    public boolean usernameExists(String username) {
        Integer count = jdbcTemplate.queryForObject(USERNAME_EXISTS, Integer.class, username);
        return count != null && count > 0;
    }

    public User findUserByUsername(String username) {
        Map<String, Object> row = jdbcTemplate.queryForMap(FIND_BY_USERNAME, username);

        UUID userId = (UUID) row.get("user_id");
        String foundUsername = (String) row.get("username");
        String hashedPassword = (String) row.get("password");


        return new User(userId, foundUsername, hashedPassword);
    }

    public void deleteUser(String username, String rawPassword) {
        String sql = "SELECT user_id, password FROM users WHERE username = ?";

        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, username);

            UUID userId = (UUID) result.get("user_id");
            String storedHashedPassword = (String) result.get("password");

            if (passwordEncoder.matches(rawPassword, storedHashedPassword)) {
                jdbcTemplate.update(DELETE_USER, userId);
            } else {
                throw new RuntimeException("Incorrect password");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("User not found");
        }
    }

}
