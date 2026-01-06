package com.noted.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noted.dto.UserRequest;
import com.noted.models.User;
import com.noted.services.UserFolderService;
import com.noted.services.UserService;

@RestController
@RequestMapping("/noted/users")
public class UserController {

    private final UserService userService;
    private final UserFolderService userFolderService;

    public UserController(UserService userService, UserFolderService userFolderService) {
        this.userService = userService;
        this.userFolderService = userFolderService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRequest request) {
        try {
            User user = userService.createUser(request.username(), request.password());

            // create a user folder when a user is created
            userFolderService.createUserFolder(user.getUserId());

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserRequest request) {
        try {
            User user = userService.login(request.username(), request.password());

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // add a logout route and JWT
    @PostMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount(@RequestBody UserRequest request) {
        try {
            userService.deleteUser(request.username(), request.password());

            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        }
    }
}
