package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.model.TodoShareDeleteRequest;
import com.robottx.todoservice.model.TodoShareRequest;
import com.robottx.todoservice.model.TodoShareResponse;

import java.util.List;

public interface TodoShareService {

    List<TodoShareResponse> getTodoShares(Long todoId);

    void shareTodo(Long todoId, TodoShareRequest request);

    void deleteShare(Long todoId, TodoShareDeleteRequest request);

}
