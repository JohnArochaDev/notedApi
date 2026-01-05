package com.noted.services;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noted.Dao.UserDao;
import com.noted.models.User;

@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String rawPassword) {
        if (userDao.usernameExists(username)) {
            throw new RuntimeException("Username Already Taken");
        }

        UUID userId = UUID.randomUUID();

        String hashedPassword = passwordEncoder.encode(rawPassword);

        userDao.insertUser(userId, username, hashedPassword);

        return new User(userId, username, null);
    }

}
