package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.Todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
