package com.cleoaguiar.todolistapi.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;
}
