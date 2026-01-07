package com.noted.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noted.dto.NewFolderRequest;
import com.noted.dto.NewNodeFileRequest;
import com.noted.models.UserFolder;
import com.noted.services.FolderService;
import com.noted.services.NodeFileService;
import com.noted.services.UserFolderService;
import com.noted.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/noted")
public class UserFolderController {

    private final FolderService folderService;
    private final UserFolderService userFolderService;
    private final NodeFileService nodeFileService;

    public UserFolderController(UserService userService, FolderService folderService, UserFolderService userFolderService, NodeFileService nodeFileService) {
        this.folderService = folderService;
        this.userFolderService = userFolderService;
        this.nodeFileService = nodeFileService;
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

            folderService.createFolder(userFolderId, (UUID) body.parent_id(), (String) body.name());

            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create folder: " + e.getMessage());
        }
    }

    @PostMapping("node-files")
    public ResponseEntity<?> createNodeFile(@RequestBody NewNodeFileRequest body) {
        try {
            nodeFileService.createNodeFile(body.parent_id(), body.name());

            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create node file: " + e.getMessage());

        }
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
