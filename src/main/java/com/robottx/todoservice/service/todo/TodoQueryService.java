package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import org.springframework.data.domain.Page;

public interface TodoQueryService {

    Page<TodoAccess> findAllTodos(String userId, SearchRequest searchRequest, SearchMode searchMode);

    TodoAccess findTodoById(String userId, Long todoId);

}
