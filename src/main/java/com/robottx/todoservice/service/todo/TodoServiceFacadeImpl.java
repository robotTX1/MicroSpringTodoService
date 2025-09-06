package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.model.CreateTodoRequest;
import com.robottx.todoservice.model.PatchTodoRequest;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.model.TodoResponse;
import com.robottx.todoservice.model.UpdateTodoRequest;
import com.robottx.todoservice.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoServiceFacadeImpl implements TodoServiceFacade {

    private final TodoQueryService todoQueryService;
    private final TodoManagementService todoManagementService;
    private final TodoResponseMapperService todoResponseMapperService;
    private final SecurityService securityService;

    @Override
    public PagedModel<TodoResponse> findAllTodos(SearchRequest searchRequest, SearchMode searchMode) {
        Page<TodoAccess> pagedTodoAccesses =
                todoQueryService.findAllTodos(securityService.getId(), searchRequest, searchMode);
        return todoResponseMapperService.mapTodoAccessesToResponses(pagedTodoAccesses);
    }

    @Override
    public TodoResponse findTodoById(Long id) {
        TodoAccess todoAccess = todoQueryService.findTodoById(securityService.getId(), id);
        return todoResponseMapperService.mapTodoAccessToTodoResponse(todoAccess);
    }

    @Override
    public TodoResponse createTodo(CreateTodoRequest request) {
        TodoAccess todoAccess = todoManagementService.createTodo(securityService.getId(), request);
        return todoResponseMapperService.mapTodoAccessToTodoResponse(todoAccess);
    }

    @Override
    public TodoResponse updateTodo(Long id, UpdateTodoRequest request) {
        TodoAccess todoAccess = todoManagementService.updateTodo(securityService.getId(), id, request);
        return todoResponseMapperService.mapTodoAccessToTodoResponse(todoAccess);
    }

    @Override
    public TodoResponse patchTodo(Long id, PatchTodoRequest request) {
        TodoAccess todoAccess = todoManagementService.patchTodo(securityService.getId(), id, request);
        return todoResponseMapperService.mapTodoAccessToTodoResponse(todoAccess);
    }

    @Override
    public void deleteTodo(Long id) {
        todoManagementService.deleteTodo(securityService.getId(), id);
    }

}
