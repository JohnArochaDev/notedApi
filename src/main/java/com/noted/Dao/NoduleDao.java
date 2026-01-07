package com.noted.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noted.models.Coordinates;
import com.noted.models.Nodule;

@Repository
public class NoduleDao {

    private final JdbcTemplate jdbcTemplate;

    public NoduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_NODULES_BY_PARENT_ID
            = "SELECT * FROM nodule WHERE parent_id = ?;";

    private static final String INSERT_NODULE
            = "INSERT INTO nodule (id, parent_id, type, x, y, width, height, text_content) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    public void insertNodule(UUID id, UUID parent_id, Integer x, Integer y, Integer width, Integer height, String text_content) {
        jdbcTemplate.update(INSERT_NODULE, id, parent_id, "textNode", x, y, width, height, text_content);
    }

    public Nodule[] getNodulesByParentId(UUID parent_id) {
        List<Nodule> results = jdbcTemplate.query(
                GET_NODULES_BY_PARENT_ID,
                (rs, _) -> new Nodule(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("parent_id")),
                        rs.getString("type"),
                        new Coordinates(rs.getInt("x"), rs.getInt("y")),
                        rs.getInt("width"),
                        rs.getInt("height"),
                        null
                )
        );

        return results.toArray(Nodule[]::new);
    }
}
