package com.cleoaguiar.todolistapi.repository;

import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUser(User user);
}
