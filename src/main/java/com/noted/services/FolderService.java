package com.noted.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.noted.Dao.FolderDao;
import com.noted.Dao.NodeFileDao;
import com.noted.models.Folder;
import com.noted.models.NodeFile;
import com.noted.models.UserFolder;

@Service
public class FolderService {

    private final FolderDao folderDao;
    private final NodeFileDao nodeFileDao;

    public FolderService(FolderDao folderDao, NodeFileDao nodeFileDao) {
        this.folderDao = folderDao;
        this.nodeFileDao = nodeFileDao;
    }

    public UserFolder getUserFolderHierarchy(UUID userFolderId) {
        List<Folder> allFolders = folderDao.getFoldersByUserFolderId(userFolderId);
        List<NodeFile> allNodes = nodeFileDao.getNodesByUserFolderId(userFolderId);

        if (allFolders == null || allFolders.isEmpty()) {
            return new UserFolder(userFolderId, getCurrentUserId(), new Folder[0]);
        }

        // Build lookup maps
        Map<UUID, List<Folder>> childrenMap = new HashMap<>();
        Map<UUID, List<NodeFile>> nodesMap = new HashMap<>();

        // Initialize empty lists for every folder
        for (Folder folder : allFolders) {
            childrenMap.put(folder.getId(), new ArrayList<>());
            nodesMap.put(folder.getId(), new ArrayList<>());
        }

        // Assign nodes to their parent folders
        for (NodeFile node : allNodes) {
            nodesMap.computeIfAbsent(node.getParentId(), k -> new ArrayList<>()).add(node);
        }

        // Link child folders to parents
        for (Folder folder : allFolders) {
            if (folder.getParentId() != null) {
                childrenMap.computeIfAbsent(folder.getParentId(), k -> new ArrayList<>())
                        .add(folder);
            }
        }

        // Build root folders with full nesting
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
        // TODO: Implement properly using JWT context (e.g., request attribute or SecurityContext)
        throw new UnsupportedOperationException("getCurrentUserId not implemented yet");
    }
}
