package com.noted.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;

import com.noted.models.UserFolder;

public class UserFolderDao {

    private final JdbcTemplate jdbcTemplate;

    public UserFolderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_USER_FOLDER = "INSERT INTO user_folder (id, user_id) VALUES (?, ?);";

    private static final String FIND_USER_FOLDER_BY_USER_ID = "SELECT * FROM user_folder WHERE user_id = ?;";

    public void creatUserFolder(UUID id, String user_id) {
        jdbcTemplate.update(INSERT_USER_FOLDER, id, user_id);
    }

    public UserFolder findUserFolder(UUID user_id) {
        List<UserFolder> results = jdbcTemplate.query(
                FIND_USER_FOLDER_BY_USER_ID,
                (rs, _) -> new UserFolder(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("user_id")),
                        null // populate the users folders here. do later when folders exist
                ),
                user_id
        );

        // should pull the function from the to-be FolderDao that will find all of the folders from a given User Folder, or I wont do that here, but in the parent where this is called, i chain that function after this one to populate that data
        return results.isEmpty() ? null : results.get(0);
    }
}
