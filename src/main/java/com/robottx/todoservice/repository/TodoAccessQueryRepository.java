package com.robottx.todoservice.repository;

import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.TodoAccess;

import org.springframework.data.domain.Page;

public interface TodoAccessQueryRepository {

    Page<TodoAccess> findAll(QueryParams<TodoAccess> queryParams);

}
