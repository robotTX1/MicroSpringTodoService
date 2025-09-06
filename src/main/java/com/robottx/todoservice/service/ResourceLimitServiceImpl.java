package com.robottx.todoservice.service;

import com.robottx.todoservice.domain.ResourceLimits;
import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.exception.InternalServerErrorException;
import com.robottx.todoservice.exception.ResourceLimitException;
import com.robottx.todoservice.repository.ResourceLimitRepository;
import com.robottx.todoservice.repository.TodoAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceLimitServiceImpl implements ResourceLimitService {

    private static final String TODO_LIMIT_REACHED = "Max Todo limit reached. Limit: %d";
    private static final String TODO_LIMIT_REACHED_LOG = "User %s has reached the maximum limit of Todos: {}";
    private static final String CATEGORY_LIMIT_REACHED = "Max Category limit reached for Todo. Limit: %d";
    private static final String CATEGORY_LIMIT_REACHED_LOG = "User %s has reached the maximum limit of Category: {}";
    private static final String SHARE_LIMIT_REACHED = "Max Share limit reached for Todo. Limit: %d";
    private static final String SHARE_LIMIT_REACHED_LOG = "User %s has reached the maximum limit of Shares: {} on Todo %d";

    private final TodoAccessRepository todoAccessRepository;
    private final ResourceLimitRepository resourceLimitRepository;

    @Override
    public void validateTodoResourceLimit(String userId) {
        Integer todoCount = todoAccessRepository.countByUserIdAndAccessLevel(userId, UserAccessLevels.OWNER.getLevel()) + 1;
        validate(ResourceLimits.TODO_LIMIT, todoCount, TODO_LIMIT_REACHED, TODO_LIMIT_REACHED_LOG.formatted(userId));
    }

    @Override
    public void validateCategoryLimit(String userId, int count) {
        validate(ResourceLimits.CATEGORY_LIMIT, count, CATEGORY_LIMIT_REACHED, CATEGORY_LIMIT_REACHED_LOG.formatted(userId));
    }

    @Override
    public void validateShareLimit(String userId, String otherUserId, Long todoId) {
        Optional<TodoAccess> todoAccess = todoAccessRepository.findByUserIdAndTodoId(otherUserId, todoId);
        if (todoAccess.isPresent()) {
            return;
        }
        Integer shareCount = todoAccessRepository.countAllByTodoId(todoId);
        validate(ResourceLimits.SHARE_LIMIT, shareCount, SHARE_LIMIT_REACHED, SHARE_LIMIT_REACHED_LOG.formatted(userId, todoId));
    }

    private void validate(ResourceLimits resourceLimit, Integer count, String message, String logMessage) {
        Integer limit = getMaxResourceLimit(resourceLimit);
        if (limit < count) {
            log.debug(logMessage, resourceLimit);
            throw new ResourceLimitException(message.formatted(limit));
        }
    }

    private Integer getMaxResourceLimit(ResourceLimits resourceLimit) {
        return resourceLimitRepository.findByResource(resourceLimit.getName())
                .orElseThrow(InternalServerErrorException::new).getMaxNumber();
    }

}
