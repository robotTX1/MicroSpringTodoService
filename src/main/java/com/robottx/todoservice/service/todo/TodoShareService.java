package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.model.TodoShareDeleteRequest;
import com.robottx.todoservice.model.TodoShareRequest;
import com.robottx.todoservice.model.TodoShareResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface TodoShareService {

    PagedModel<TodoShareResponse> getTodoShares(Long todoId, Pageable pageable);

    void shareTodo(Long todoId, TodoShareRequest request);

    void deleteShare(Long todoId, TodoShareDeleteRequest request);

}
