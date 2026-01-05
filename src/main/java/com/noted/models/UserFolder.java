package com.noted.models;

import java.util.Arrays;
import java.util.UUID;

public class UserFolder {

    public UUID id;
    public Folder[] folders;

    public UserFolder(UUID id, Folder[] folders) {
        this.id = id;
        this.folders = folders;
    }

    public UUID getId() {
        return id;
    }

    public Folder[] getFolders() {
        return folders;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFolders(Folder[] folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return "UserFolder{"
                + "id=" + id
                + ", folders=" + Arrays.toString(folders)
                + '}';
    }
}
