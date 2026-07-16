package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}