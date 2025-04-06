package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.Category;
import com.robottx.todoservice.entity.Priority;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.entity.UserAccessLevel;
import com.robottx.todoservice.exception.ResourceCannotBeDeletedException;
import com.robottx.todoservice.model.CreateTodoRequest;
import com.robottx.todoservice.model.UpdateTodoRequest;
import com.robottx.todoservice.repository.TodoAccessRepository;
import com.robottx.todoservice.repository.TodoRepository;
import com.robottx.todoservice.service.CategoryService;
import com.robottx.todoservice.service.PriorityService;
import com.robottx.todoservice.service.ResourceLimitService;
import com.robottx.todoservice.service.UserAccessLevelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoManagementServiceImpl implements TodoManagementService {

    private static final List<String> NULLABLE_FIELDS = List.of("parent", "deadline");

    private final TodoRepository todoRepository;
    private final TodoAccessRepository todoAccessRepository;
    private final TodoValidationService todoValidationService;
    private final CategoryService categoryService;
    private final UserAccessLevelService userAccessLevelService;
    private final PriorityService priorityService;
    private final ResourceLimitService resourceLimitService;

    @Override
    @Transactional
    public TodoAccess createTodo(String userId, CreateTodoRequest request) {
        log.debug("Creating todo for user {}", userId);
        resourceLimitService.validateTodoResourceLimit(userId);
        Set<Category> categories = createCategories(userId, request.getCategories());
        Priority priority = priorityService.getPriorityByLevel(request.getPriority());
        Todo parent = todoValidationService.validateTodoParent(userId, request.getParent());
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .completed(request.getCompleted())
                .categories(categories)
                .priority(priority)
                .owner(userId)
                .shared(false)
                .parent(parent)
                .build();
        Todo savedTodo = todoRepository.save(todo);
        UserAccessLevel ownerAccessLevel = userAccessLevelService.findByLevel(UserAccessLevels.OWNER.getLevel());
        TodoAccess todoAccess = TodoAccess.builder()
                .todo(savedTodo)
                .userId(userId)
                .accessLevel(ownerAccessLevel)
                .build();
        TodoAccess savedTodoAccess = todoAccessRepository.save(todoAccess);
        log.debug("Successfully created todo for user {}", userId);
        return savedTodoAccess;
    }

    @Override
    @Transactional
    public TodoAccess updateTodo(String userId, Long todoId, UpdateTodoRequest request) {
        TodoAccess todoAccess = todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.WRITE);
        Set<Category> categories = createCategories(userId, request.getCategories());
        Priority priority = priorityService.getPriorityByLevel(request.getPriority());
        Todo parent = todoValidationService.validateTodoParent(userId, request.getParent());
        Todo todo = todoAccess.getTodo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDeadline(request.getDeadline());
        todo.setCompleted(request.getCompleted());
        todo.setCategories(categories);
        todo.setPriority(priority);
        todo.setParent(parent);
        todoRepository.save(todo);
        log.debug("Successfully updated todo with id {} by user {}", todoId, userId);
        return todoAccess;
    }

    @Override
    @Transactional
    public TodoAccess patchTodo(String userId, Long todoId, UpdateTodoRequest request) {
        TodoAccess todoAccess = todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.WRITE);
        Todo todo = todoAccess.getTodo();
        try {
            Field[] declaredFields = UpdateTodoRequest.class.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(request);
                if (fieldValue == null && !NULLABLE_FIELDS.contains(fieldName)) {
                    continue;
                }
                Field todoField;
                try {
                    todoField = Todo.class.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ex) {
                    continue;
                }
                todoField.setAccessible(true);
                switch (fieldName) {
                    case "parent" -> patchTodoParent(userId, request, todo);
                    case "priority" -> todo.setPriority(priorityService.getPriorityByLevel(request.getPriority()));
                    case "categories" -> todo.setCategories(createCategories(userId, request.getCategories()));
                    case "deadline" -> patchDeadline(request, todo);
                    default -> todoField.set(todo, fieldValue);
                }
            }
            todoRepository.save(todo);
            log.debug("Successfully patched todo with id {} by user {}", todoId, userId);
        } catch (IllegalAccessException ex) {
            log.error("Failed to path todo {} by user {}", todoId, userId, ex);
            throw new RuntimeException(ex);
        }
        return todoAccess;
    }

    @Override
    @Transactional
    public void deleteTodo(String userId, Long todoId) {
        TodoAccess todoAccess = todoValidationService.validateUserAccess(userId, todoId, UserAccessLevels.MANAGE);
        if (todoAccessRepository.countAllByParentId(todoId) > 0) {
            log.error("User {} failed to delete todo by id {} because other todos reference it", userId, todoId);
            throw new ResourceCannotBeDeletedException("Todo can't be deleted because it is referenced by other todos");
        }
        Todo todo = todoAccess.getTodo();
        Set<TodoAccess> todoAccesses = todoAccessRepository.findAllByTodoId(todo.getId());
        log.debug("User {} is deleting {} todo access objects", userId, todoAccesses.size());
        todoAccessRepository.deleteAll(todoAccesses);
        categoryService.deleteCategoryConnections(todo.getId());
        todoRepository.delete(todo);
        log.debug("Todo with id {} successfully deleted by user {}", todoId, userId);
    }

    private void patchTodoParent(String userId, UpdateTodoRequest request, Todo todo) {
        if (request.getParent() != null) {
            if (request.getParent() == -1L) {
                todo.setParent(null);
            } else {
                Todo parent = todoValidationService.validateTodoParent(userId, request.getParent());
                todo.setParent(parent);
            }
        }
    }

    private void patchDeadline(UpdateTodoRequest request, Todo todo) {
        if (request.getDeadline() != null && !request.getDeadline().equals(UpdateTodoRequest.DEFAULT_DEADLINE)) {
            todo.setDeadline(request.getDeadline());
        }
    }

    private Set<Category> createCategories(String userId, Set<String> categories) {
        resourceLimitService.validateCategoryLimit(userId, categories.size());
        return categoryService.createCategories(categories);
    }

}
