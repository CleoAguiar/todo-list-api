package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.TodoResponse;
import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.enums.TodoStatus;
import com.cleoaguiar.todolistapi.exception.TodoNotFoundException;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        lenient().when(authentication.getPrincipal()).thenReturn(user);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldCreateTodoSuccessfully() {
        TodoRequest request = new TodoRequest(
                "Test Title",
                "Buy coffee",
                TodoStatus.TODO);

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

    @Test
    void shouldUpdateTodoSuccessfully() {
        TodoRequest request = new TodoRequest(
                "Test Updated Title",
                "Buy coffee latte",
                TodoStatus.IN_PROGRESS);

        User savedUser = new User();
        savedUser.setEmail("cleo@email.com");

        Todo existingTodo = new Todo();
        existingTodo.setTitle("Test Title");
        existingTodo.setDescription("Buy coffee");
        existingTodo.setStatus(TodoStatus.TODO);
        existingTodo.setUser(savedUser);

        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Test Updated Title");
        updatedTodo.setDescription("Buy coffee latte");
        updatedTodo.setStatus(TodoStatus.IN_PROGRESS);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        TodoResponse result = todoService.update(1L, request);

        assertNotNull(result);
        assertEquals("Test Updated Title", result.title());
        assertEquals("Buy coffee latte", result.description());
        assertEquals(TodoStatus.IN_PROGRESS, result.status());

        verify(todoRepository).findById(1L);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void shouldDeleteTodoSuccessfully() {
        User savedUser = new User();
        savedUser.setEmail("cleo@email.com");

        Todo existingTodo = new Todo();
        existingTodo.setTitle("Test Title");
        existingTodo.setDescription("Buy coffee");
        existingTodo.setStatus(TodoStatus.TODO);
        existingTodo.setUser(savedUser);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));

        todoService.delete(1L);

        verify(todoRepository).findById(1L);
        verify(todoRepository).delete(any(Todo.class));
    }

    @Test
    void shouldGetTodoByIdSuccessfully() {
        User savedUser = new User();
        savedUser.setEmail("cleo@email.com");

        Todo existingTodo = new Todo();
        existingTodo.setTitle("Test Title");
        existingTodo.setDescription("Buy coffee");
        existingTodo.setStatus(TodoStatus.TODO);
        existingTodo.setUser(savedUser);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));

        TodoResponse result = todoService.getById(1L);

        assertNotNull(result);
        assertEquals("Test Title", result.title());
        assertEquals("Buy coffee", result.description() );
        assertEquals(TodoStatus.TODO, result.status());

        verify(todoRepository).findById(1L);
    }

    @Test
    void shouldGetAllTodosSuccessfully() {
        Todo existingTodo = new Todo();
        existingTodo.setTitle("Test Title");
        existingTodo.setDescription("Buy coffee");
        existingTodo.setStatus(TodoStatus.TODO);

        List<Todo> todoList = List.of(existingTodo);

        Page<Todo> page = new PageImpl<>(todoList);

        when(todoRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(page);

        Page<TodoResponse> result = todoService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1L, result.getTotalElements());
        assertEquals("Test Title", result.getContent().get(0).title());

        verify(todoRepository).findAllByUser(any(User.class), any(Pageable.class));
    }

    @Test
    void shouldThrowTodoNotFoundExceptionWhenTodoDoesNotExist() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        TodoNotFoundException exception = assertThrows(
                TodoNotFoundException.class,
                () -> todoService.getById(1L)
        );

        assertEquals("Todo com id 1 não encontrado.", exception.getMessage());

        verify(todoRepository).findById(1L);
    }
}
