package com.noted.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noted.models.Coordinates;
import com.noted.models.Nodule;
import com.noted.models.NoduleData;

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

    private static final String UPDATE_NODULE
            = "UPDATE nodule SET x = ?, y = ?, width = ?, height = ?, text_content = ? WHERE id = ?;";

    private static final String NODULE_EXISTS
            = "SELECT COUNT(*) FROM nodule WHERE id = ?;";

    public Nodule insertNodule(UUID id, UUID parent_id, Integer x, Integer y, Integer width, Integer height, String text_content) {
        jdbcTemplate.update(INSERT_NODULE, id, parent_id, "textNode", x, y, width, height, text_content);

        Integer integerWidth = width;
        Integer integerHeight = height;

        return new Nodule(id, parent_id, "textNode", new Coordinates(x, y), integerWidth, integerHeight, new NoduleData(text_content));
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
                ),
                parent_id
        );

        return results.toArray(Nodule[]::new);
    }

    public void updateNoduleById(UUID id, int x, int y, int width, int height, String text_content) {
        jdbcTemplate.update(UPDATE_NODULE, x, y, width, height, text_content, id);
    }

    public boolean noduleExistsById(UUID id) {
        Integer count = jdbcTemplate.queryForObject(NODULE_EXISTS, Integer.class, id);

        return count != null && count > 0;
    }
}
