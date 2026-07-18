package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.AuthRequest;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(UserRegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return repository.save(user);
    }

    public void login(AuthRequest authRequest) {
        repository.findByEmail(authRequest.getEmail()).
                orElseThrow(()-> new IllegalArgumentException("E-mail ou senha inválidos"));
    }
}
