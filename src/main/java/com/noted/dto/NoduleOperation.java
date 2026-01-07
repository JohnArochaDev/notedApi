package com.noted.dto;

import java.util.UUID;

public record NoduleOperation(
        UUID id, // null → create, present → update
        UUID parentId, // required for create, ignored for update
        int x,
        int y,
        int width,
        int height,
        String textContent
        ) {

}
