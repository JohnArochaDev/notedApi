package com.noted.Dao;

import java.util.List;
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

    private static final String USER_ID_EXISTS = "SELECT COUNT(*) FROM users WHERE user_id = ?";

    private static final String USERNAME_EXISTS
            = "SELECT COUNT(*) FROM users WHERE username = ?";

    private static final String UPDATE_PASSWORD_BY_USER_ID = "UPDATE users SET password = ? WHERE user_id = ?;";

    public void insertUser(UUID userId, String username, String hashedPassword) {
        jdbcTemplate.update(INSERT_USER, userId, username, hashedPassword);
    }

    public boolean usernameExists(String username) {
        Integer count = jdbcTemplate.queryForObject(USERNAME_EXISTS, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean userIdExists(UUID userId) {
        Integer count = jdbcTemplate.queryForObject(USER_ID_EXISTS, Integer.class, userId);
        return count != null && count > 0;
    }

    public User findUserByUsername(String username) {
        List<User> results = jdbcTemplate.query(
                FIND_BY_USERNAME,
                (rs, _) -> new User(
                        UUID.fromString(rs.getString("user_id")),
                        rs.getString("username"),
                        rs.getString("password")
                ),
                username
        );

        return results.isEmpty() ? null : results.get(0);
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

    public void updatePassword(String newPassword, UUID user_id) {
        int rowsAffected = jdbcTemplate.update(UPDATE_PASSWORD_BY_USER_ID, newPassword, user_id);

        if (rowsAffected == 0) {
            throw new RuntimeException("User not found or update failed");
        }
    }
}
