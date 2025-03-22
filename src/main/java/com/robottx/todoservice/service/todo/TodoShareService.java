package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.model.TodoShareDeleteRequest;
import com.robottx.todoservice.model.TodoShareRequest;

public interface TodoShareService {

    void shareTodo(Long todoId, TodoShareRequest request);

    void deleteShare(Long todoId, TodoShareDeleteRequest request);

}
