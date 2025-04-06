package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.mapper.TodoMapper;
import com.robottx.todoservice.mapper.UserAccessLevelMapper;
import com.robottx.todoservice.model.TodoResponse;
import com.robottx.todoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoResponseMapperServiceImpl implements TodoResponseMapperService {

    private final UserService userService;

    @Override
    public TodoResponse mapTodoAccessToTodoResponse(TodoAccess todoAccess) {
        TodoResponse todoResponse = TodoMapper.INSTANCE.domainToModel(todoAccess.getTodo());
        todoResponse.setAccessLevel(UserAccessLevelMapper.INSTANCE.domainToModel(todoAccess.getAccessLevel()));
        todoResponse.setOwnerEmail(userService.getUserEmail(todoAccess.getTodo().getOwner()));
        return todoResponse;
    }

    @Override
    public PagedModel<TodoResponse> mapTodoAccessesToToResponses(Page<TodoAccess> pagedTodoAccesses) {
        List<TodoResponse> todoResponses = pagedTodoAccesses.getContent().stream()
                .map(this::mapTodoAccessToTodoResponse)
                .toList();
        PageImpl<TodoResponse> pagedTodoResponses =
                new PageImpl<>(todoResponses, pagedTodoAccesses.getPageable(), pagedTodoAccesses.getTotalElements());
        return new PagedModel<>(pagedTodoResponses);
    }

}
