package com.noted.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.noted.Dao.FolderDao;
import com.noted.Dao.NodeFileDao;
import com.noted.Dao.UserFolderDao;
import com.noted.models.Folder;
import com.noted.models.NodeFile;
import com.noted.models.UserFolder;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class FolderService {

    private final FolderDao folderDao;
    private final NodeFileDao nodeFileDao;
    private final UserFolderDao userFolderDao;

    public FolderService(FolderDao folderDao, NodeFileDao nodeFileDao, UserFolderDao userFolderDao) {
        this.folderDao = folderDao;
        this.nodeFileDao = nodeFileDao;
        this.userFolderDao = userFolderDao;
    }

    public UserFolder getUserFolderHierarchy(UUID userFolderId) {
        List<Folder> allFolders = folderDao.getFoldersByUserFolderId(userFolderId);
        List<NodeFile> allNodes = nodeFileDao.getNodesByUserFolderId(userFolderId);

        if (allFolders == null || allFolders.isEmpty()) {
            return new UserFolder(userFolderId, getCurrentUserId(), new Folder[0]);
        }

        // build lookup maps
        Map<UUID, List<Folder>> childrenMap = new HashMap<>();
        Map<UUID, List<NodeFile>> nodesMap = new HashMap<>();

        // initialize empty lists for every folder
        for (Folder folder : allFolders) {
            childrenMap.put(folder.getId(), new ArrayList<>());
            nodesMap.put(folder.getId(), new ArrayList<>());
        }

        // assign nodes to their parent folders
        for (NodeFile node : allNodes) {
            nodesMap.computeIfAbsent(node.getParentId(), k -> new ArrayList<>()).add(node);
        }

        // link child folders to parents
        for (Folder folder : allFolders) {
            if (folder.getParentId() != null) {
                childrenMap.computeIfAbsent(folder.getParentId(), k -> new ArrayList<>())
                        .add(folder);
            }
        }

        // build root folders with full nesting
        List<Folder> rootFolders = new ArrayList<>();
        for (Folder folder : allFolders) {
            if (folder.getParentId() == null) {
                rootFolders.add(buildNestedFolder(folder, childrenMap, nodesMap));
            }
        }

        Folder[] rootArray = rootFolders.toArray(Folder[]::new);

        return new UserFolder(userFolderId, getCurrentUserId(), rootArray);
    }

    private Folder buildNestedFolder(
            Folder base,
            Map<UUID, List<Folder>> childrenMap,
            Map<UUID, List<NodeFile>> nodesMap) {

        List<Folder> childList = childrenMap.getOrDefault(base.getId(), List.of());
        Folder[] subfolders = childList.stream()
                .map(child -> buildNestedFolder(child, childrenMap, nodesMap))
                .toArray(Folder[]::new);

        List<NodeFile> nodeList = nodesMap.getOrDefault(base.getId(), List.of());
        NodeFile[] nodes = nodeList.toArray(NodeFile[]::new);
        return new Folder(
                base.getId(),
                base.getParentId(),
                base.getName(),
                subfolders,
                nodes
        );
    }

    private UUID getCurrentUserId() {
        ServletRequestAttributes attributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No HTTP request context available — this method must be called during a web request");
        }

        HttpServletRequest request = attributes.getRequest();
        Object userIdObj = request.getAttribute("userId");

        if (userIdObj == null) {
            throw new IllegalStateException("Authenticated user ID not found — invalid or missing JWT");
        }

        if (!(userIdObj instanceof UUID)) {
            throw new IllegalStateException("Invalid user ID type in request attribute");
        }

        return (UUID) userIdObj;
    }

    public Folder createFolder(UUID userFolderId, UUID parentId, String name) {
        if (!userFolderDao.userFolderExistsById(userFolderId)) {
            throw new RuntimeException("user folder does not exist");
        }
        UUID id = UUID.randomUUID();

        return folderDao.createFolder(id, userFolderId, parentId, name);
    }

    public void updateFolder(UUID id, String name) {
        boolean folder = folderDao.folderExistsById(id);

        if (!folder) {
            throw new RuntimeException("folder does not exist");
        }

        folderDao.updateFolderById(id, name);
    }

    public void deleteFolder(UUID id) {
        boolean folder = folderDao.folderExistsById(id);

        if (!folder) {
            throw new RuntimeException("folder does not exist");
        }

        folderDao.deleteFolderById(id);
    }

}
