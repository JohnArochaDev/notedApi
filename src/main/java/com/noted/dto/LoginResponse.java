package com.noted.dto;

import com.noted.models.User;

public record LoginResponse(String token, User user) {}
