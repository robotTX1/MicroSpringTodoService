package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;

public interface TodoValidationService {

    Todo validateTodoParent(String userId, Long parentId);

    TodoAccess validateUserAccess(String userId, Long todoId, UserAccessLevels minimumAccessLevel);

    void validateUserIsNotOwner(TodoAccess todoAccess);

}
