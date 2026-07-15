package com.cleoaguiar.todolistapi.dto;

import com.cleoaguiar.todolistapi.enums.TodoStatus;

import java.time.LocalDateTime;

public record TodoResponse (
        Long id,
        String title,
        String description,
        TodoStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
