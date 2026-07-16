package com.cleoaguiar.todolistapi.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegisterRequest {
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
