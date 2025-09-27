package com.robottx.todoservice.mapper;

import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.model.TodoResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Component;

@Component
public class TodoResponseMapper {

    public TodoResponse mapTodoAccessToTodoResponse(TodoAccess todoAccess) {
        TodoResponse todoResponse = TodoMapper.INSTANCE.domainToModel(todoAccess.getTodo());
        todoResponse.setAccessLevel(todoAccess.getAccessLevel().getAccessLevel());
        return todoResponse;
    }

    public PagedModel<TodoResponse> mapTodoAccessesToResponses(Page<TodoAccess> pagedTodoAccesses) {
        List<TodoResponse> todoResponses = pagedTodoAccesses.getContent().stream()
                .map(this::mapTodoAccessToTodoResponse)
                .toList();
        PageImpl<TodoResponse> pagedTodoResponses =
                new PageImpl<>(todoResponses, pagedTodoAccesses.getPageable(), pagedTodoAccesses.getTotalElements());
        return new PagedModel<>(pagedTodoResponses);
    }

}
