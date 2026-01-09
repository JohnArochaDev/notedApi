package com.noted.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.noted.Dao.NodeFileDao;
import com.noted.Dao.NoduleDao;
import com.noted.models.Nodule;

@Service
public class NoduleService {

    private final NoduleDao noduleDao;
    private final NodeFileDao nodeFileDao;

    public NoduleService(NoduleDao noduleDao, NodeFileDao nodeFileDao) {
        this.noduleDao = noduleDao;
        this.nodeFileDao = nodeFileDao;
    }

    public Nodule createNodule(UUID parentId, int x, int y, int width, int height, String textContent) {
        boolean nodeFile = nodeFileDao.nodeFileExistsById(parentId);
        if (!nodeFile) {
            throw new RuntimeException("No file exists to save to");
        }

        UUID id = UUID.randomUUID();

        return noduleDao.insertNodule(id, parentId, x, y, width, height, textContent);
    }

    public void updateNodule(UUID id, int x, int y, int width, int height, String textContent) {

        boolean nodule = noduleDao.noduleExistsById(id);

        if (!nodule) {
            throw new RuntimeException("No node exists to update");
        }

        noduleDao.updateNoduleById(id, x, y, width, height, textContent);
    }

    public Nodule[] getNodulesByParentId(UUID parentId) {
        boolean nodeFile = nodeFileDao.nodeFileExistsById(parentId);

        if (!nodeFile) {
            throw new RuntimeException("No file exists to save to");
        }

        return noduleDao.getNodulesByParentId(parentId);
    }

    public void deleteAllNodulesByParentId(UUID parentId) {
        noduleDao.deleteAllNodulesByParentId(parentId);
    }

}
