package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.model.CreateTodoRequest;
import com.robottx.todoservice.model.PatchTodoRequest;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.model.TodoResponse;
import com.robottx.todoservice.model.UpdateTodoRequest;

import org.springframework.data.web.PagedModel;

public interface TodoServiceFacade {

    PagedModel<TodoResponse> findAllTodos(SearchRequest searchRequest, SearchMode searchMode);

    TodoResponse findTodoById(Long id);

    TodoResponse createTodo(CreateTodoRequest request);

    TodoResponse updateTodo(Long id, UpdateTodoRequest request);

    TodoResponse patchTodo(Long id, PatchTodoRequest request);

    void deleteTodo(Long id);

}
