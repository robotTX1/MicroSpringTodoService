package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.TodoAccess;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TodoAccessRepository extends JpaRepository<TodoAccess, Long>, JpaSpecificationExecutor<TodoAccess> {

    @Query("""
            SELECT ta FROM TodoAccess ta
            JOIN FETCH ta.accessLevel a
            JOIN FETCH ta.todo t
            JOIN FETCH t.priority
            WHERE ta.userId = :userId
            AND t.id = :todoId
            """)
    Optional<TodoAccess> findByUserIdAndTodoId(String userId, Long todoId);

    @Query("""
            SELECT ta FROM TodoAccess ta
            JOIN FETCH ta.accessLevel a
            JOIN FETCH ta.todo t
            WHERE t.id = :todoId
            ORDER BY a.accessLevel DESC
            """)
    List<TodoAccess> findAllByTodoId(Long todoId);

    @Query("""
            SELECT ta FROM TodoAccess ta
            JOIN FETCH ta.accessLevel a
            JOIN FETCH ta.todo t
            WHERE t.id = :todoId
            ORDER BY a.accessLevel DESC
            """)
    Page<TodoAccess> findAllByTodoIdPage(Long todoId, Pageable pageable);

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

    @Query("""
            SELECT COUNT(*) FROM TodoAccess ta
            INNER JOIN UserAccessLevel a ON ta.accessLevel.id = a.id
            WHERE ta.userId = :userId
            AND a.accessLevel = :accessLevel
            """)
    Integer countByUserIdAndAccessLevel(String userId, Integer accessLevel);

}
