package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.entity.UserAccessLevel;
import com.robottx.todoservice.mapper.UserAccessLevelMapper;
import com.robottx.todoservice.model.TodoShareDeleteRequest;
import com.robottx.todoservice.model.TodoShareRequest;
import com.robottx.todoservice.model.TodoShareResponse;
import com.robottx.todoservice.repository.TodoAccessRepository;
import com.robottx.todoservice.repository.TodoRepository;
import com.robottx.todoservice.security.SecurityService;
import com.robottx.todoservice.service.ResourceLimitService;
import com.robottx.todoservice.service.UserAccessLevelService;
import com.robottx.todoservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoShareServiceImpl implements TodoShareService {

    private final TodoRepository todoRepository;
    private final TodoAccessRepository todoAccessRepository;
    private final TodoValidationService todoValidationService;
    private final SecurityService securityService;
    private final UserService userService;
    private final UserAccessLevelService userAccessLevelService;
    private final ResourceLimitService resourceLimitService;

    @Override
    public List<TodoShareResponse> getTodoShares(Long todoId) {
        String userId = securityService.getId();
        log.debug("Getting shares for todo with id {} by user {}", todoId, userId);
        todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.READ);
        List<TodoShareResponse> shares = todoAccessRepository.findAllByTodoId(todoId).stream()
                .sorted(Comparator.comparing(t -> t.getAccessLevel().getAccessLevel(), Comparator.reverseOrder()))
                .map(this::mapToTodoShareResponse)
                .toList();
        log.debug("Got {} shares for todo with id {} by user {}", shares.size(), todoId, userId);
        return shares;
    }

    @Override
    @Transactional
    public void shareTodo(Long todoId, TodoShareRequest request) {
        String userId = securityService.getId();
        log.debug("Sharing todo with id {} to user {} by {}", todoId, request.getEmail(), userId);
        TodoAccess todoAccess = todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.MANAGE);
        String userIdByEmail = userService.getUserIdByEmail(request.getEmail());
        todoAccessRepository.findByUserIdAndTodoId(userIdByEmail, todoId)
                .ifPresent(todoValidationService::validateUserIsNotOwner);
        resourceLimitService.validateShareLimit(userId, userIdByEmail, todoId);
        Todo todo = todoAccess.getTodo();
        todo.setShared(true);
        TodoAccess newTodoAccess = TodoAccess.builder()
                .todo(todo)
                .userId(userIdByEmail)
                .accessLevel(userAccessLevelService.findByLevel(request.getAccessLevel()))
                .build();
        todoAccessRepository.save(newTodoAccess);
        log.debug("Successfully shared todo {} with user {} by {}", todoId, request.getEmail(), userId);
    }

    @Override
    @Transactional
    public void deleteShare(Long todoId, TodoShareDeleteRequest request) {
        String userId = securityService.getId();
        String userIdByEmail = userService.getUserIdByEmail(request.getEmail());
        if (userId.equals(userIdByEmail)) {
            deleteSelfFromShare(userId, todoId);
        } else {
            deleteShareForOtherUser(userId, userIdByEmail, todoId);
        }
    }

    private TodoShareResponse mapToTodoShareResponse(TodoAccess todoAccess) {
        return TodoShareResponse.builder()
                .email(userService.getUserEmail(todoAccess.getUserId()))
                .accessLevel(UserAccessLevelMapper.INSTANCE.domainToModel(todoAccess.getAccessLevel()))
                .build();
    }

    private void deleteSelfFromShare(String userId, Long todoId) {
        log.debug("Attempting to delete share for self {} for todo {}", userId, todoId);
        TodoAccess todoAccess = todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.READ);
        UserAccessLevel accessLevel = todoAccess.getAccessLevel();
        if (UserAccessLevels.OWNER.getLevel() == accessLevel.getAccessLevel()) {
            log.error("User {} tried to delete owner share for todo {}", userId, todoId);
            throw new IllegalArgumentException("Can't delete owner access");
        }
        Todo todo = todoAccess.getTodo();
        todoAccessRepository.delete(todoAccess);
        log.debug("Successfully deleted share for self {} for todo {}", userId, todoId);
        disableShareFlag(todo);
        removeInaccessibleParent(todoId, userId);
    }

    private void deleteShareForOtherUser(String userId, String otherUserId, Long todoId) {
        log.debug("Attempting to delete share for user {} for todo {} by user {}", otherUserId, todoId, userId);
        todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.MANAGE);
        TodoAccess todoAccess = todoAccessRepository.findByUserIdAndTodoId(otherUserId, todoId)
                .orElseThrow(() -> new IllegalArgumentException("User is not found"));
        todoValidationService.validateUserIsNotOwner(todoAccess);
        Todo todo = todoAccess.getTodo();
        todoAccessRepository.delete(todoAccess);
        log.debug("Successfully deleted share for user {} for todo {} by user {}", otherUserId, todoId, userId);
        disableShareFlag(todo);
        removeInaccessibleParent(todoId, otherUserId);
    }

    private void disableShareFlag(Todo todo) {
        Integer shareCount = todoAccessRepository.countAllByTodoId(todo.getId());
        log.debug("Todo {} is shared with {} users", todo.getId(), shareCount);
        if (shareCount == 1) {
            log.debug("Setting shared flag to false for todo {}", todo.getId());
            todo.setShared(false);
            todoRepository.save(todo);
        }
    }

    private void removeInaccessibleParent(Long todoId, String userId) {
        Set<Todo> todos = todoAccessRepository.findAllByUserIdAndParentId(userId, todoId)
                .stream()
                .map(TodoAccess::getTodo)
                .collect(Collectors.toSet());
        log.debug("User {} has {} todos that reference parent todo {}", userId, todos.size(), todoId);
        todos.forEach(t -> t.setParent(null));
        todoRepository.saveAll(todos);
    }

}
