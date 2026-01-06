package com.noted.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;

import com.noted.models.Folder;

public class FolderDao {

    private final JdbcTemplate jdbcTemplate;

    public FolderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE_FOLDER = "INSERT INTO folder (id, user_folder_id, parent_id, name, type) VALUES (?, ?, ?, ?, folder);";

    private static final String GET_FOLDERS_BY_USER_FOLDER_ID = "SELECT * FROM folders WHERE user_folder_id = ?;";

    public void createFolder(UUID id, UUID user_folder_id, UUID parent_id, String name) {
        jdbcTemplate.update(CREATE_FOLDER, id, user_folder_id, parent_id, name, "folder");
    }

    public List<Folder> getFoldersByUserFolderId(UUID user_folder_id) {
        List<Folder> results = jdbcTemplate.query(
                GET_FOLDERS_BY_USER_FOLDER_ID,
                (rs, _) -> new Folder(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("parent_id")),
                        rs.getString("name"),
                        null,
                        null
                )
        );

        return results.isEmpty() ? null : results;
    }

}
