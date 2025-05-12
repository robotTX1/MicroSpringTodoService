package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.TodoAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface TodoAccessRepository extends JpaRepository<TodoAccess, Long>, JpaSpecificationExecutor<TodoAccess> {

    @EntityGraph(attributePaths = {"todo", "accessLevel", "todo.priority", "todo.categories"})
    Page<TodoAccess> findAll(Specification<TodoAccess> spec, Pageable pageable);

    @Query("""
            SELECT ta FROM TodoAccess ta
            JOIN FETCH ta.accessLevel a
            JOIN FETCH ta.todo t
            JOIN FETCH t.priority
            WHERE ta.userId = :userId
            AND t.id = :todoId
            """)
    Optional<TodoAccess> findByUserIdAndTodoId(String userId, Long todoId);

    Set<TodoAccess> findAllByTodoId(Long todoId);

    @Query("""
            SELECT ta FROM TodoAccess ta
            INNER JOIN Todo t ON ta.todo.id = t.id
            WHERE t.parent.id = :todoId
            AND ta.userId = :userId
            """)
    Set<TodoAccess> findAllByUserIdAndParentId(String userId, Long todoId);

    @Query("""
            SELECT COUNT(*) FROM TodoAccess ta
            INNER JOIN Todo t ON ta.todo.id = t.id
            WHERE t.parent.id = :todoId
            """)
    Integer countAllByParentId(Long todoId);

    @Query("""
            SELECT COUNT(*) FROM TodoAccess ta
            INNER JOIN Todo t ON ta.todo.id = t.id
            WHERE ta.todo.id = :todoId
            """)
    Integer countAllByTodoId(Long todoId);

}
