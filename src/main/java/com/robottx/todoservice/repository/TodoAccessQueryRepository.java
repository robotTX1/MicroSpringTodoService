package com.robottx.todoservice.repository;

import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.TodoAccess;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface TodoAccessQueryRepository {

    Page<TodoAccess> findAll(QueryParams<TodoAccess> queryParams);

    List<Long> findAllIdsBySpec(Specification<TodoAccess> spec);

}
