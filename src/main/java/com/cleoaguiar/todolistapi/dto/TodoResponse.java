package com.cleoaguiar.todolistapi.dto;

import com.cleoaguiar.todolistapi.enums.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TodoResponse(
        @Schema(description = "ID da tarefa", example = "1")
        Long id,

        @Schema(description = "Título da tarefa", example = "Estudar Spring Boot", maxLength = 100)
        String title,

        @Schema(description = "Descrição detalhada", example = "Revisar OpenAPI", maxLength = 500)
        String description,

        @Schema(description = "Status da tarefa", allowableValues = {"TODO", "IN_PROGRESS", "DONE"})
        TodoStatus status,

        @Schema(description = "Data de criação da tarefa", example = "2026-08-15T14:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Data da última alteração da tarefa", example = "2026-08-15T16:45:30")
        LocalDateTime updatedAt
) {
}
