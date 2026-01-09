package com.noted.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Nodule {

    @JsonProperty
    private final UUID id;

    @JsonProperty
    private final UUID parentId;

    @JsonProperty
    private final String type;

    @JsonProperty
    private final Coordinates coordinates;

    @JsonProperty
    private final int width;

    @JsonProperty
    private final int height;

    @JsonProperty
    private final NoduleData data;

    public Nodule(UUID id, UUID parentId, String type, Coordinates coordinates,
            int width, int height, NoduleData data) {
        this.id = id;
        this.parentId = parentId;
        this.type = type != null ? type : "textNode";
        this.coordinates = coordinates;
        this.width = width;
        this.height = height;
        this.data = data != null ? data : new NoduleData(""); // never null
    }

    public UUID getId() {
        return id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public String getType() {
        return type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public NoduleData getData() {
        return data;
    }

    // Optional: toString for debugging
    @Override
    public String toString() {
        return "Nodule{"
                + "id=" + id
                + ", parentId=" + parentId
                + ", type='" + type + '\''
                + ", coordinates=" + coordinates
                + ", width=" + width
                + ", height=" + height
                + ", data=" + data
                + '}';
    }
}
