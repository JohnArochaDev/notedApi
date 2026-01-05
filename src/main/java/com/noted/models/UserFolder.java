package com.noted.models;

import java.util.Arrays;
import java.util.UUID;

public class UserFolder {

    public UUID id;
    public UUID userId;
    public Folder[] folders;

    public UserFolder(UUID id, UUID userId, Folder[] folders) {
        this.id = id;
        this.userId = userId;
        this.folders = folders;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Folder[] getFolders() {
        return folders;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setFolders(Folder[] folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return "UserFolder{"
                + "id=" + id
                + ", userId=" + userId
                + ", folders=" + Arrays.toString(folders)
                + '}';
    }
}
