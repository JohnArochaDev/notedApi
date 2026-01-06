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

    public List<NodeFile> getNodesByUserFolderId(UUID userFolderId) {
        String sql = """
            SELECT nf.id, nf.parent_id, nf.name, nf.type
            FROM node_file nf
            JOIN folder f ON nf.parent_id = f.id
            WHERE f.user_folder_id = ?
            """;

        return jdbcTemplate.query(
                sql,
                (rs, _) -> new NodeFile(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("parent_id")),
                        rs.getString("name"),
                        NodeFileType.valueOf(rs.getString("type"))
                ),
                userFolderId
        );
    }
}
