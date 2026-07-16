package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(UserRegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Nome de usuário já cadastrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        return repository.save(user);
    }
}
