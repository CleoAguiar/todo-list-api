package com.cleoaguiar.todolistapi.dto;

import com.cleoaguiar.todolistapi.enums.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoRequest(
        @Schema(description = "Título da tarefa", example = "Estudar Spring Boot", maxLength = 100)
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
        String title,

        @Schema(description = "Descrição detalhada", example = "Revisar OpenAPI", maxLength = 500)
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String description,

        @Schema(description = "Status da tarefa", allowableValues = {"TODO", "IN_PROGRESS", "DONE"})
        TodoStatus status
) {
}
