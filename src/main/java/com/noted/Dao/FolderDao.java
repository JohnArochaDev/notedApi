package com.noted.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noted.models.Folder;

@Repository
public class FolderDao {

    private final JdbcTemplate jdbcTemplate;

    public FolderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE_FOLDER
            = "INSERT INTO folder (id, user_folder_id, parent_id, name, type) VALUES (?, ?, ?, ?, ?);";

    private static final String GET_FOLDERS_BY_USER_FOLDER_ID
            = "SELECT * FROM folder WHERE user_folder_id = ?;";

    private static final String UPDATE_FOLDER
            = "UPDATE folder SET name = ? WHERE id = ?;";

    private static final String DELETE_FOLDER_BY_ID
            = "DELETE FROM folder WHERE id = ?;";

    private static final String FOLDER_EXISTS = "SELECT COUNT(*) FROM folder WHERE id = ?;";

    public Folder createFolder(UUID id, UUID user_folder_id, UUID parent_id, String name) {
        jdbcTemplate.update(CREATE_FOLDER, id, user_folder_id, parent_id, name, "folder");

        return new Folder(id, parent_id, name, null, null);
    }

    public List<Folder> getFoldersByUserFolderId(UUID userFolderId) {
        return jdbcTemplate.query(
                GET_FOLDERS_BY_USER_FOLDER_ID,
                (rs, _) -> {
                    String parentIdStr = rs.getString("parent_id");
                    UUID parentId = parentIdStr == null ? null : UUID.fromString(parentIdStr);

                    return new Folder(
                            UUID.fromString(rs.getString("id")),
                            parentId,
                            rs.getString("name"),
                            null,
                            null
                    );
                },
                userFolderId
        );
    }

    public boolean folderExistsById(UUID id) {
        Integer count = jdbcTemplate.queryForObject(FOLDER_EXISTS, Integer.class, id);

        return count != null && count > 0;
    }

    public void updateFolderById(UUID id, String name) {
        jdbcTemplate.update(UPDATE_FOLDER, name, id);
    }

    public void deleteFolderById(UUID id) {
        jdbcTemplate.update(DELETE_FOLDER_BY_ID, id);
    }
}
