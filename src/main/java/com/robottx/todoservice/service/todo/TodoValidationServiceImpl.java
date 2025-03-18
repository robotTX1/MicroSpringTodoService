package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import com.robottx.todoservice.repository.TodoAccessRepository;
import com.robottx.todoservice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoValidationServiceImpl implements TodoValidationService {

    private final TodoRepository todoRepository;
    private final TodoAccessRepository todoAccessRepository;

    @Override
    public Todo validateTodoParent(String userId, Long parentId) {
        Todo todo = null;
        if (parentId != null) {
            todo = todoRepository.findByIdAndOwner(parentId, userId)
                    .orElseThrow(NotFoundOrUnauthorizedException::new);
        }
        return todo;
    }

    @Override
    public TodoAccess validateUserAccess(String userId, Long todoId, UserAccessLevels minimumAccessLevel) {
        TodoAccess todoAccess = todoAccessRepository.findByUserIdAndTodoId(userId, todoId)
                .orElseThrow(NotFoundOrUnauthorizedException::new);
        if (todoAccess.getAccessLevel().getAccessLevel() < minimumAccessLevel.getLevel()) {
            log.error("User {} does not have required access level [{}] to modify todo with id {}", userId, minimumAccessLevel, todoId);
            throw new NotFoundOrUnauthorizedException();
        }
        return todoAccess;
    }

}
