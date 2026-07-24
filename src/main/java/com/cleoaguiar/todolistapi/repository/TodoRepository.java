package com.cleoaguiar.todolistapi.repository;

import com.cleoaguiar.todolistapi.entity.Todo;
import com.cleoaguiar.todolistapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findAllByUser(User user, Pageable pageable);
}
