package com.noted.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noted.Dao.UserDao;
import com.noted.Dao.UserFolderDao;

@Service
@Transactional
public class UserFolderService {

    private final UserFolderDao userFolderDao;
    private final UserDao userDao;

    public UserFolderService(UserFolderDao userFolderDao, UserDao userDao) {
        this.userFolderDao = userFolderDao;
        this.userDao = userDao;
    }

    public void createUserFolder(UUID userId) {
        UUID id = UUID.randomUUID();

        if (!userDao.userIdExists(userId)) {
            throw new RuntimeException("User does not exist");
        }

        userFolderDao.createUserFolder(id, userId);
    }

    public UUID findUserFolderIdByUserId(UUID userId) {
        return userFolderDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("UserFolder not found for user: " + userId));
    }
}
