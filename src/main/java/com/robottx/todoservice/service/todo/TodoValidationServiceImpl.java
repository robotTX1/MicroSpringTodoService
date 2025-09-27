package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.exception.ModifyOwnershipException;
import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import com.robottx.todoservice.exception.ValidationException;
import com.robottx.todoservice.repository.TodoAccessRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoValidationServiceImpl implements TodoValidationService {

    private final TodoAccessRepository todoAccessRepository;

    @Override
    public Todo validateTodoParent(String userId, Long parentId) {
        Todo todo = null;
        if (parentId != null) {
            todo = todoAccessRepository.findByUserIdAndTodoId(userId, parentId)
                    .orElseThrow(NotFoundOrUnauthorizedException::new)
                    .getTodo();
        }
        return todo;
    }

    @Override
    public TodoAccess validateUserAccess(String userId, Long todoId, UserAccessLevels minimumAccessLevel) {
        TodoAccess todoAccess = todoAccessRepository.findByUserIdAndTodoId(userId, todoId)
                .orElseThrow(NotFoundOrUnauthorizedException::new);
        if (todoAccess.getAccessLevel().getAccessLevel() < minimumAccessLevel.getLevel()) {
            log.error("User {} does not have required access level [{}] to modify todo with id {}", userId,
                    minimumAccessLevel, todoId);
            throw new NotFoundOrUnauthorizedException();
        }
        return todoAccess;
    }

    @Override
    public void validateUserIsNotOwner(TodoAccess todoAccess) {
        if (todoAccess.getAccessLevel().getAccessLevel() == UserAccessLevels.OWNER.getLevel()) {
            throw new ModifyOwnershipException("Cannot change owner's access level");
        }
    }

    @Override
    public void validateTodoTitleAndDescription(Todo todo) {
        if (StringUtils.isBlank(todo.getTitle()) && StringUtils.isBlank(todo.getDescription())) {
            throw new ValidationException("Title or description must not be blank");
        }
    }

}
