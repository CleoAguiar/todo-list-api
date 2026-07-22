package com.cleoaguiar.todolistapi.entity;

import jakarta.persistence.*;

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

    public Long getId() { return id; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return email; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() { return password; }
}


