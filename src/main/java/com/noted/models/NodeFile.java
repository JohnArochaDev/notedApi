package com.noted.models;

import java.util.UUID;

public class NodeFile {

    public UUID id;
    public UUID parentId;
    public String name;
    public NodeFileType type = NodeFileType.node;
    public Nodule[] nodules;

    public NodeFile(UUID id, UUID parentId, String name, NodeFileType type, Nodule[] nodules) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.nodules = nodules;
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

    public Nodule[] getNodules() {
        return nodules;
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

    public void setNodules(Nodule[] nodules) {
        this.nodules = nodules;
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
