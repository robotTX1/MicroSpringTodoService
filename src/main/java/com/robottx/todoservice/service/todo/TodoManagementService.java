package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.model.CreateTodoRequest;
import com.robottx.todoservice.model.PatchTodoRequest;
import com.robottx.todoservice.model.UpdateTodoRequest;

public interface TodoManagementService {

    TodoAccess createTodo(String userId, CreateTodoRequest request);

    TodoAccess updateTodo(String userId, Long todoId, UpdateTodoRequest request);

    TodoAccess patchTodo(String userId, Long todoId, PatchTodoRequest request);

    void deleteTodo(String userId, Long todoId);


}
