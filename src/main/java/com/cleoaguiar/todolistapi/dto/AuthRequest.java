package com.cleoaguiar.todolistapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @Schema(description = "E-mail do usuário", example = "jose@email.com")
        @Email(message = "E-mail inválido.")
        @NotBlank(message = "O e-mail é obrigatório.")
        String email,

        @Schema(description = "Senha do usuário", example = "password")
        @NotBlank(message = "A senha é obrigatória.")
        String password
) {
}
