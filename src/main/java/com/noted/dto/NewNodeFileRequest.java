package com.noted.dto;

import java.util.UUID;

public record NewNodeFileRequest(UUID parent_id, String name) {}