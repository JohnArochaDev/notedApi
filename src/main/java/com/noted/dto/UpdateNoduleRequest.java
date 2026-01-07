package com.noted.dto;

import java.util.UUID;

public record UpdateNoduleRequest(UUID id, int x, int y, int width, int height, String textContent) {}