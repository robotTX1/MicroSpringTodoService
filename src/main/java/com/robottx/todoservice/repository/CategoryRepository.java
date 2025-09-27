package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.Category;
import com.robottx.todoservice.entity.TodoWithCategory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            SELECT NEW com.robottx.todoservice.entity.TodoWithCategory(ct.todoId, c) FROM Category c
            INNER JOIN CategoryTodo ct ON c.id = ct.categoryId
            WHERE ct.todoId IN :ids
            """)
    List<TodoWithCategory> findAllByTodoIds(Set<Long> ids);

    @Query("""
            SELECT c FROM Category c
            INNER JOIN CategoryTodo ct ON c.id = ct.categoryId
            WHERE ct.todoId = :todoId
            """)
    LinkedHashSet<Category> findAllByTodoId(Long todoId);

    LinkedHashSet<Category> findAllByNameIn(Set<String> categories);

    @Modifying
    @Query("""
            DELETE FROM CategoryTodo ct
            WHERE ct.todoId = :todoId
            """)
    void deleteAllByTodoId(Long todoId);

}
