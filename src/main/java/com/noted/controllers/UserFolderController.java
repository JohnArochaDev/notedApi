package com.noted.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.noted.dto.DeleteFolderOrNodeRequest;
import com.noted.dto.NewFolderRequest;
import com.noted.dto.NewNodeFileRequest;
import com.noted.dto.NoduleOperation;
import com.noted.dto.UpdateFolderRequest;
import com.noted.dto.UpdateNodeRequest;
import com.noted.models.Folder;
import com.noted.models.NodeFile;
import com.noted.models.Nodule;
import com.noted.models.UserFolder;
import com.noted.services.FolderService;
import com.noted.services.NodeFileService;
import com.noted.services.NoduleService;
import com.noted.services.UserFolderService;
import com.noted.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/noted")
public class UserFolderController {

    private final FolderService folderService;
    private final UserFolderService userFolderService;
    private final NodeFileService nodeFileService;
    private final NoduleService noduleService;

    public UserFolderController(UserService userService, FolderService folderService, UserFolderService userFolderService, NodeFileService nodeFileService, NoduleService noduleService) {
        this.folderService = folderService;
        this.userFolderService = userFolderService;
        this.nodeFileService = nodeFileService;
        this.noduleService = noduleService;
    }

    @GetMapping("/folders")
    public ResponseEntity<UserFolder> getUserFolderHierarchy(HttpServletRequest request) {
        // extract current user ID from JWT (set in JwtFilter)
        UUID currentUserId = getCurrentUserId(request);

        // dind this user's UserFolder ID
        UUID userFolderId = userFolderService.findUserFolderIdByUserId(currentUserId);

        // build and return the full nested hierarchy
        UserFolder hierarchy = folderService.getUserFolderHierarchy(userFolderId);

        return ResponseEntity.ok(hierarchy);
    }

    @PostMapping("/folders")
    public ResponseEntity<?> createFolder(@RequestBody NewFolderRequest body,
            HttpServletRequest request) {
        try {
            UUID currentUserId = getCurrentUserId(request);

            UUID userFolderId = userFolderService.findUserFolderIdByUserId(currentUserId);

            Folder folder = folderService.createFolder(userFolderId, (UUID) body.parent_id(), (String) body.name());

            return ResponseEntity.ok().body(folder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create folder: " + e.getMessage());
        }
    }

    @PutMapping("/folders")
    public ResponseEntity<?> updateFolder(@RequestBody UpdateFolderRequest body) {
        try {
            folderService.updateFolder((UUID) body.id(), (String) body.name());

            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update folder: " + e.getMessage());
        }
    }

    @DeleteMapping("/folders")
    public ResponseEntity<?> updateFolder(@RequestBody DeleteFolderOrNodeRequest body) {
        try {
            folderService.deleteFolder((UUID) body.id());

            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete folder: " + e.getMessage());
        }
    }

    @PostMapping("node-files")
    public ResponseEntity<?> createNodeFile(@RequestBody NewNodeFileRequest body) {
        try {
            NodeFile nodeFile = nodeFileService.createNodeFile(body.parent_id(), body.name());

            return ResponseEntity.ok().body(nodeFile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create node file: " + e.getMessage());

        }
    }

    @PutMapping("node-files")
    public ResponseEntity<?> updateNodeFile(@RequestBody UpdateNodeRequest body) {
        try {
            nodeFileService.updateNodeFile(body.id(), body.name());

            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update node file: " + e.getMessage());

        }
    }

    @DeleteMapping("/node-files")
    public ResponseEntity<?> deleteNodeFile(@RequestBody DeleteFolderOrNodeRequest body) {
        try {
            nodeFileService.deleteNodeFile((UUID) body.id());

            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete node file: " + e.getMessage());
        }
    }

    @GetMapping("/nodule")
    public ResponseEntity<Nodule[]> getNodulesByParentId(
            @RequestParam("parentId") UUID parentId) {

        Nodule[] nodules = noduleService.getNodulesByParentId(parentId);
        return ResponseEntity.ok(nodules);
    }

    @PostMapping("/nodules")
    public ResponseEntity<Nodule[]> saveNodules(
            @RequestBody List<NoduleOperation> operations,
            HttpServletRequest request) {

        UUID parentId = operations.stream()
                .map(op -> op.id() == null ? op.parentId() : null)
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("At least one create operation must provide parentId"));

        for (NoduleOperation op : operations) {
            if (op.id() == null) {
                // CREATE
                if (op.parentId() == null) {
                    throw new IllegalArgumentException("parentId required for new nodule");
                }
                noduleService.createNodule(
                        op.parentId(),
                        op.x(),
                        op.y(),
                        op.width(),
                        op.height(),
                        op.textContent()
                );
            } else {
                // UPDATE
                noduleService.updateNodule(
                        op.id(),
                        op.x(),
                        op.y(),
                        op.width(),
                        op.height(),
                        op.textContent()
                );
            }
        }

        Nodule[] updatedNodules = noduleService.getNodulesByParentId(parentId);

        return ResponseEntity.ok(updatedNodules);
    }

    private UUID getCurrentUserId(HttpServletRequest request) {
        Object userIdObj = request.getAttribute("userId");

        if (userIdObj == null) {
            throw new IllegalStateException("No authenticated user found — invalid or missing JWT");
        }

        if (!(userIdObj instanceof UUID)) {
            throw new IllegalStateException("Invalid user ID type in request context");
        }

        return (UUID) userIdObj;
    }
}
