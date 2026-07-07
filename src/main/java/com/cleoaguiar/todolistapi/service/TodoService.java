package com.cleoaguiar.todolistapi.service;

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

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<Todo> getAll() {
        return repository.findAll();
    }

    public Todo create(Todo todo) {
        return repository.save(todo);
    }

    public Todo getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    public void delete(Long id) {
        Todo todo = repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        repository.delete(todo);
    }

    public Todo update(Long id, Todo updatedTodo) {
        Todo todo = repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));

        todo.setTitle(updatedTodo.getTitle());
        todo.setDescription(updatedTodo.getDescription());
        todo.setUpdatedAt();

        return repository.save(todo);
    }
}
