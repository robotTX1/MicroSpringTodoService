package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.TodoAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TodoAccessQueryRepository {

    List<TodoAccess> findAll(Specification<TodoAccess> spec, Pageable pageable);

    Long getTotalElements(Specification<TodoAccess> spec);

}
