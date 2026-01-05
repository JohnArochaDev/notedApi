package com.noted.models;

import java.util.UUID;

public class Nodule {

    public UUID id;
    public UUID parentId;
    public String type = "textNode";
    public Coordinates coordinates;
    public int width;
    public int height;
    public NoduleData data;

    public Nodule(UUID id, UUID parentId, String type, Coordinates coordinates,
            int width, int height, NoduleData data) {
        this.id = id;
        this.parentId = parentId;
        this.type = type;
        this.coordinates = coordinates;
        this.width = width;
        this.height = height;
        this.data = data;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setData(NoduleData data) {
        this.data = data;
    }

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
