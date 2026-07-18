package com.cleoaguiar.todolistapi.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegisterRequest {
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String username;

    @NotBlank
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() { return email; }

    public String getPassword() {
        return password;
    }
}
