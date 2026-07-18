package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.AuthRequest;
import com.cleoaguiar.todolistapi.dto.AuthResponse;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserRegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }

    public void login(AuthRequest authRequest) {
        User user = repository.findByEmail(authRequest.getEmail())
                        .orElseThrow(() ->
                                new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        // gerar JWT

        // retornar token
    }
}
