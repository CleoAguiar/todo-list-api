package com.cleoaguiar.todolistapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) {
        this.password = password;
    }
}


