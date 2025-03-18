package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Long> {

    Optional<Priority> findByPriorityLevel(Integer priorityLevel);

}
