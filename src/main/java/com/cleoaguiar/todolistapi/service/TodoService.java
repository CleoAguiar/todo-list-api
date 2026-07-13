package com.cleoaguiar.todolistapi.service;

import com.cleoaguiar.todolistapi.dto.TodoRequest;
import com.cleoaguiar.todolistapi.dto.TodoResponse;
import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.exception.TodoNotFoundException;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repository;

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TodoResponse create(TodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());

        Todo savedTodo = repository.save(todo);

        return toResponse(savedTodo);
    }

    public TodoResponse getById(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        return toResponse(todo);
    }

    public void delete(Long id) {
        Todo todo = repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        repository.delete(todo);
    }

    public TodoResponse update(Long id, TodoRequest request) {
        Todo existingTodo = repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));

        existingTodo.setTitle(request.title());
        existingTodo.setDescription(request.description());

        Todo updateTodo = repository.save(existingTodo);

        return toResponse(updateTodo);
    }
}
