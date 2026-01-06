package com.noted.dto;

public record UserChangePassword(String username, String oldPassword, String newPassword) {}
