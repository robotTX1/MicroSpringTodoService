package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.ResourceLimit;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceLimitRepository extends JpaRepository<ResourceLimit, Long> {

    Optional<ResourceLimit> findByResource(String resource);

}
