package com.robottx.todoservice.repository;

import com.robottx.todoservice.domain.GroupingOptions;
import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.entity.TodoStatistics;

import org.springframework.transaction.annotation.Transactional;

public interface TodoStatisticsRepository {

    TodoStatistics basicQuery(QueryParams<TodoAccess> queryParams);

    @Transactional(readOnly = true)
    TodoStatistics queryByGroup(QueryParams<TodoAccess> queryParams, GroupingOptions groupingOption);

}
