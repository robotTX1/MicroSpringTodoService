package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
            SELECT t FROM Todo t
            JOIN FETCH t.priority
            WHERE t.owner = :ownerId
            AND t.id = :id
            """)
    Optional<Todo> findByIdAndOwner(Long id, String ownerId);

}
