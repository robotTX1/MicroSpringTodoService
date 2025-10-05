package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.Category;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.exception.InvalidSearchQueryException;
import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.repository.TodoAccessQueryRepository;
import com.robottx.todoservice.repository.TodoAccessRepository;
import com.robottx.todoservice.service.CategoryService;
import com.robottx.todoservice.util.QueryParamsUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import cz.jirutka.rsql.parser.RSQLParserException;
import io.github.perplexhub.rsql.PropertyBlacklistedException;
import io.github.perplexhub.rsql.UnknownPropertyException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoQueryServiceImpl implements TodoQueryService {

    private final TodoAccessQueryRepository todoAccessQueryRepository;
    private final TodoAccessRepository todoAccessRepository;
    private final CategoryService categoryService;

    @Override
    public Page<TodoAccess> findAllTodos(String userId, SearchRequest searchRequest, SearchMode searchMode) {
        log.debug("Querying todos for user {} with search {} and search mode: {}", userId, searchRequest, searchMode);
        Page<TodoAccess> pagedTodos = queryTodos(userId, searchRequest, searchMode);
        log.debug("Found {} todos for user {}", pagedTodos.getContent().size(), userId);
        return pagedTodos;
    }

    @Override
    public TodoAccess findTodoById(String userId, Long todoId) {
        log.debug("Getting todo {} by user {}", todoId, userId);
        Optional<TodoAccess> todoOptional = todoAccessRepository.findByUserIdAndTodoId(userId, todoId);
        return todoOptional
                .map(this::addCategoryToTodo)
                .orElseThrow(() -> {
                    log.debug("No todo found with id {} for user {}", todoId, userId);
                    return new NotFoundOrUnauthorizedException("No todo found with id: " + todoId);
                });
    }

    private Page<TodoAccess> queryTodos(String userId, SearchRequest searchRequest, SearchMode searchMode) {
        try {
            QueryParams<TodoAccess> queryParams = QueryParamsUtil.createQueryParams(userId, searchRequest, searchMode);
            Page<TodoAccess> todos = todoAccessQueryRepository.findAll(queryParams);
            long totalElements = todos.getTotalElements();
            return new PageImpl<>(addCategoriesToTodos(todos.getContent()), queryParams.getPageable(), totalElements);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid search request: {} by user {}", searchRequest, userId);
            throw new InvalidSearchQueryException(ex.getMessage(), ex);
        } catch (UnknownPropertyException | PropertyBlacklistedException ex) {
            log.error("User {} tried to query prohibited property: {}", userId, ex.getName());
            throw new InvalidSearchQueryException("Unknown property: %s".formatted(ex.getName()), ex);
        } catch (RSQLParserException ex) {
            log.error("Parsing failed for search request: {} by user {}", searchRequest, userId);
            throw new InvalidSearchQueryException("Failed to parse search request", ex);
        }
    }

    private TodoAccess addCategoryToTodo(TodoAccess todoAccess) {
        Todo todo = todoAccess.getTodo();
        Set<Category> categories = categoryService.findCategoriesByTodoId(todo.getId());
        todo.setCategories(categories);
        return todoAccess;
    }

    private List<TodoAccess> addCategoriesToTodos(List<TodoAccess> todoAccesses) {
        Set<Long> todoIds = todoAccesses.stream()
                .map(t -> t.getTodo().getId()).collect(Collectors.toSet());
        Map<Long, Set<Category>> categoryMap = categoryService.findCategoriesForTodos(todoIds);
        todoAccesses.forEach(t -> t.getTodo()
                .setCategories(categoryMap.getOrDefault(t.getTodo().getId(), Collections.emptySet())));
        return todoAccesses;
    }

}
