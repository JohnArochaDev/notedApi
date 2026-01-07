package com.noted.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noted.models.NodeFile;
import com.noted.models.NodeFileType;

@Repository
public class NodeFileDao {

    private final JdbcTemplate jdbcTemplate;

    public NodeFileDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String NODE_FILE_EXISTS
            = "SELECT COUNT(*) FROM node_file WHERE id = ?;";

    private static final String INSERT_NODE_FILE
            = "INSERT INTO node_file (id, parent_id, name, type) VALUES (?, ?, ?, ?);";

    private static final String UPDATE_NODE_BY_ID
            = "UPDATE node_file SET name = ? WHERE id = ?;";

    private static final String GET_NODES_BY_USER_FOLDER_ID = """
        SELECT 
            node_file.id,
            node_file.parent_id,
            node_file.name,
            node_file.type
        FROM node_file
        JOIN folder ON node_file.parent_id = folder.id
        WHERE folder.user_folder_id = ?
        """;

    public List<NodeFile> getNodesByUserFolderId(UUID userFolderId) {
        return jdbcTemplate.query(
                GET_NODES_BY_USER_FOLDER_ID,
                (rs, _) -> new NodeFile(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("parent_id")),
                        rs.getString("name"),
                        NodeFileType.valueOf(rs.getString("type")),
                        null // this can be populated later
                ),
                userFolderId
        );
    }

    public NodeFile createNodeFile(UUID id, UUID parent_id, String name) {
        jdbcTemplate.update(INSERT_NODE_FILE, id, parent_id, name, "node");

        return new NodeFile(id, parent_id, name, NodeFileType.node, null);
    }

    public void updateNodeById(UUID id, String name) {
        jdbcTemplate.update(UPDATE_NODE_BY_ID, name, id);
    }

    public boolean nodeFileExistsById(UUID id) {
        Integer count = jdbcTemplate.queryForObject(NODE_FILE_EXISTS, Integer.class, id);

        return count != null && count > 0;
    }
}
