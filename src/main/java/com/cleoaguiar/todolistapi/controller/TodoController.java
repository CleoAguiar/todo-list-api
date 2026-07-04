package com.cleoaguiar.todolistapi.controller;

import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.repository.TodoRepository;
import com.cleoaguiar.todolistapi.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAll() {
        return todoService.getAll();
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        return todoService.create(todo);
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        return todoService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        return todoService.update(id, updatedTodo);
    }
}

