package com.noted.Dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noted.models.UserFolder;

@Repository
public class UserFolderDao {

    private final JdbcTemplate jdbcTemplate;

    public UserFolderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_USER_FOLDER
            = "INSERT INTO user_folder (id, user_id) VALUES (?, ?);";

    private static final String FIND_USER_FOLDER_ID_BY_USER_ID
            = "SELECT id FROM user_folder WHERE user_id = ?;";

    private static final String FIND_USER_FOLDER_BY_USER_ID
            = "SELECT * FROM user_folder WHERE user_id = ?;";

    private static final String USER_FOLDER_ID_EXISTS
            = "SELECT COUNT(*) FROM user_folder WHERE id = ?;";

    public void createUserFolder(UUID id, UUID userId) {
        jdbcTemplate.update(INSERT_USER_FOLDER, id, userId);
    }

    public Optional<UUID> findByUserId(UUID userId) {
        List<UUID> results = jdbcTemplate.query(
                FIND_USER_FOLDER_ID_BY_USER_ID,
                (rs, _) -> UUID.fromString(rs.getString("id")),
                userId
        );

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public UserFolder findUserFolder(UUID userId) {
        List<UserFolder> results = jdbcTemplate.query(
                FIND_USER_FOLDER_BY_USER_ID,
                (rs, _) -> new UserFolder(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("user_id")),
                        null // folders array populated later in FolderService
                ),
                userId
        );

        return results.isEmpty() ? null : results.get(0);
    }

    public boolean userFolderExistsById(UUID id) {
        Integer count = jdbcTemplate.queryForObject(USER_FOLDER_ID_EXISTS, Integer.class, id);

        return count != null && count > 0;
    }
}
