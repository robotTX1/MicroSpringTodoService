package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.ResourceLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceLimitRepository extends JpaRepository<ResourceLimit, Long> {

    Optional<ResourceLimit> findByResource(String resource);

}
