package com.cleoaguiar.todolistapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "Token Jwt gerado após autenticação", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token
) {
}
