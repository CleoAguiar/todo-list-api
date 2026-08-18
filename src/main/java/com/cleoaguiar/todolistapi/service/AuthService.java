package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.AuthRequest;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(UserRegisterRequest request) {
        if (repository.existsByEmail(request.email())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return repository.save(user);
    }

    public String login(AuthRequest authRequest) {
        User user = repository.findByEmail(authRequest.email())
                        .orElseThrow(() ->
                                new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())){
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
