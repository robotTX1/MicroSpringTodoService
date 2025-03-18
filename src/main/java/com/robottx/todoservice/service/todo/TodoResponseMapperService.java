package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.model.TodoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

public interface TodoResponseMapperService {

    TodoResponse mapTodoAccessToTodoResponse(TodoAccess todoAccess);

    PagedModel<TodoResponse> mapTodoAccessesToToResponses(Page<TodoAccess> pagedTodoAccesses);

}
