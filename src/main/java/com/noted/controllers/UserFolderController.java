package com.noted.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noted.dto.NewFolderRequest;
import com.noted.models.UserFolder;
import com.noted.services.FolderService;
import com.noted.services.UserFolderService;
import com.noted.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/noted")
public class UserFolderController {

    private final FolderService folderService;
    private final UserFolderService userFolderService;

    public UserFolderController(UserService userService, FolderService folderService, UserFolderService userFolderService) {
        this.folderService = folderService;
        this.userFolderService = userFolderService;
    }

    @GetMapping("/folders")
    public ResponseEntity<UserFolder> getUserFolderHierarchy(HttpServletRequest request) {
        // Extract current user ID from JWT (set in JwtFilter)
        UUID currentUserId = getCurrentUserId(request);

        // Find this user's UserFolder ID
        UUID userFolderId = userFolderService.findUserFolderIdByUserId(currentUserId);

        // Build and return the full nested hierarchy
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
