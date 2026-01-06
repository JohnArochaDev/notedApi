package com.noted.models;

import java.util.Arrays;
import java.util.UUID;

public class Folder {

    public UUID id;
    public UUID parentId;
    public String name;
    public NodeFileType type = NodeFileType.folder;
    public Folder[] subfolders;
    public NodeFile[] nodes;

    public Folder(UUID id, UUID parentId, String name, Folder[] subfolders, NodeFile[] nodes) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.subfolders = subfolders;
        this.nodes = nodes;
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

    public Folder[] getSubfolders() {
        return subfolders;
    }

    public NodeFile[] getNodes() {
        return nodes;
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

    public void setSubfolders(Folder[] subfolders) {
        this.subfolders = subfolders;
    }

    public void setNodes(NodeFile[] nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Folder{"
                + "id=" + id
                + ", parentId=" + parentId
                + ", name='" + name + '\''
                + ", type=" + type
                + ", subfolders=" + Arrays.toString(subfolders)
                + ", nodes=" + Arrays.toString(nodes)
                + '}';

    }
}
