package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Todo> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        return repository.save(todo);
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"));
    }
}

