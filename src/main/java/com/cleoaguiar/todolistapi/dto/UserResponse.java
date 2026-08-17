package com.cleoaguiar.todolistapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "ID do usuário", example = "1")
        Long id,

        @Schema(description = "Nome do usuário", example = "Jose da Silva")
        String username
) {
}
