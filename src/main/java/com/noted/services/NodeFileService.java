package com.noted.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.noted.Dao.FolderDao;
import com.noted.Dao.NodeFileDao;
import com.noted.models.NodeFile;

@Service
public class NodeFileService {

    private final NodeFileDao nodeFileDao;
    private final FolderDao folderDao;

    public NodeFileService(NodeFileDao nodeFileDao, FolderDao folderDao) {
        this.nodeFileDao = nodeFileDao;
        this.folderDao = folderDao;
    }

    public NodeFile createNodeFile(UUID parent_id, String name) {
        boolean folderExists = folderDao.folderExistsById(parent_id);

        if (!folderExists) {
            throw new RuntimeException("node file needs a parent folder");
        }

        UUID id = UUID.randomUUID();

        return nodeFileDao.createNodeFile(id, parent_id, name);
    }

    public void updateNodeFile(UUID id, String name) {
        boolean nodeFileExists = nodeFileDao.nodeFileExistsById(id);

        if (!nodeFileExists) {
            throw new RuntimeException("node file doesnt exist");
        }

        nodeFileDao.updateNodeById(id, name);
    }

    public void deleteNodeFile(UUID id) {
        boolean nodeFileExists = nodeFileDao.nodeFileExistsById(id);

        if (!nodeFileExists) {
            throw new RuntimeException("node file doesnt exist");
        }

        nodeFileDao.deleteNodeById(id);
    }
}
