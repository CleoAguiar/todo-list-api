package com.cleoaguiar.todolistapi.repository;

import com.cleoaguiar.todolistapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
