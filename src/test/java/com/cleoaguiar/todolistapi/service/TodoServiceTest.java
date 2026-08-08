package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.TodoResponse;
import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.enums.TodoStatus;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    private SecurityContext securityContext;
    private Authentication authentication;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setup() {
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        User user = new User();
        user.setUsername("cleo");
        user.setEmail("cleo@email.com");
        user.setPassword("senhaCriptografada");

        when (authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldCreateTodoSuccessfully() {
        TodoRequest request = new TodoRequest("Test Title", "Buy coffee", TodoStatus.TODO);

        Todo savedTodo = new Todo();
        savedTodo.setTitle("Test Title");
        savedTodo.setDescription("Buy coffee");
        savedTodo.setStatus(TodoStatus.TODO);

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        TodoResponse result = todoService.create(request);

        assertNotNull(result);
        assertEquals("Test Title", result.title());
        assertEquals("Buy coffee", result.description());
        assertEquals(TodoStatus.TODO, result.status());

        verify(todoRepository).save(any(Todo.class));
    }
}
