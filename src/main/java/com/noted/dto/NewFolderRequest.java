package com.noted.dto;

import java.util.UUID;


public record NewFolderRequest(UUID parent_id, String name) {}
