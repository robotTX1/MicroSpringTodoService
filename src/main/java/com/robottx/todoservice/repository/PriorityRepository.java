package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.Priority;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, Long> {

    Optional<Priority> findByPriorityLevel(Integer priorityLevel);

}
