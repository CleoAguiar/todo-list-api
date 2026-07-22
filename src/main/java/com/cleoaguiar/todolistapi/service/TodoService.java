package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.TodoResponse;
import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.entity.User;
import com.cleoaguiar.todolistapi.exception.ForbiddenException;
import com.cleoaguiar.todolistapi.exception.TodoNotFoundException;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repository;

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private Todo getOwnedTodo(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        User authenticatedUser = getAuthenticatedUser();

        if(!todo.getUser().getEmail().equals(authenticatedUser.getEmail())) {
            throw new ForbiddenException();
        }
        return todo;
    }

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoResponse> getAll() {
        User authenticatedUser = getAuthenticatedUser();

        return repository.findAllByUser(authenticatedUser)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TodoResponse create(TodoRequest request) {
        User authenticatedUser = getAuthenticatedUser();
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setUser(authenticatedUser);

        Todo savedTodo = repository.save(todo);

        return toResponse(savedTodo);
    }

    public TodoResponse getById(Long id) {
        return toResponse(getOwnedTodo(id));
    }

    public void delete(Long id) {
        Todo todo = getOwnedTodo(id);
        repository.delete(todo);
    }

    public TodoResponse update(Long id, TodoRequest request) {
        Todo existingTodo = getOwnedTodo(id);

        existingTodo.setTitle(request.title());
        existingTodo.setDescription(request.description());

        Todo updateTodo = repository.save(existingTodo);

        return toResponse(updateTodo);
    }
}
