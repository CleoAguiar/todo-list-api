package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.UserRegisterRequest;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegisterRequest request = new UserRegisterRequest(
                "cleo",
                "cleo@email.com",
                "123456"
        );

        User savedUser = new User();
        savedUser.setUsername("cleo");
        savedUser.setEmail("cleo@email.com");
        savedUser.setPassword("senhaCodificada");

        when(userRepository.existsByEmail("cleo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("senhaCodificada");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.register(request);

        assertNotNull(result);
        assertEquals("cleo", result.getUsername());
        assertEquals("cleo@email.com", result.getEmail());
        assertEquals("senhaCodificada", result.getPassword());

        verify(userRepository).existsByEmail("cleo@email.com");
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserRegisterRequest request = new UserRegisterRequest(
                "cleo",
                "cleo@email.com",
                "123456"
        );

        when(userRepository.existsByEmail("cleo@email.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );

        assertEquals("E-mail já cadastrado", exception.getMessage());

        verify(userRepository).existsByEmail("cleo@email.com");
        verify(passwordEncoder, never()).encode((anyString()));
        verify(userRepository, never()).save(any(User.class));
    }
}
