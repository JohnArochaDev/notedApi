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

    public void deleteUser(String username, String rawPassword) {
        if (!userDao.usernameExists(username)) {
            throw new RuntimeException("Invalid username or password");
        }
        userDao.deleteUser(username, rawPassword);
    }

    public User login(String username, String rawPassword) {
        User user = userDao.findUserByUsername(username);

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            user.setPassword(null);

            return user;
        }

        throw new RuntimeException("Invalid username or password");
    }

    public void updateUserPassword(String username, String oldPassword, String newPassword) {
        User loggedInUser = login(username, oldPassword);

        if (loggedInUser == null) {
            throw new RuntimeException("Invalid username or password");
        }

        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        String newHashedPassword = passwordEncoder.encode(newPassword);

        userDao.updatePassword(newHashedPassword, loggedInUser.getUserId());
    }

}
