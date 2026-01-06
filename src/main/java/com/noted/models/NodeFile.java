package com.noted.models;

import java.util.UUID;

public class NodeFile {

    public UUID id;
    public UUID parentId;
    public String name;
    public NodeFileType type = NodeFileType.node;

    public NodeFile(UUID id, UUID parentId, String name, NodeFileType type) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public NodeFileType getType() {
        return type;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(NodeFileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NodeFile{"
                + "id=" + id
                + ", parentId=" + parentId
                + ", name='" + name + '\''
                + ", type=" + type
                + '}';
    }
}
