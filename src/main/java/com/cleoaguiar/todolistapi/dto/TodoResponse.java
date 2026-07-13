package com.cleoaguiar.todolistapi.dto;

import java.time.LocalDateTime;

public record TodoResponse (
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
