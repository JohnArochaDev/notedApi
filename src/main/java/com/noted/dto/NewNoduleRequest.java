package com.noted.dto;

import java.util.UUID;

public record NewNoduleRequest(UUID parent_id, int x, int y, int width, int height, String textContent) {}