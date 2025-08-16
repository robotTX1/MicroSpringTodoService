package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.domain.UserAccessLevels;
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
import cz.jirutka.rsql.parser.RSQLParserException;
import io.github.perplexhub.rsql.PropertyBlacklistedException;
import io.github.perplexhub.rsql.RSQLJPASupport;
import io.github.perplexhub.rsql.UnknownPropertyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoQueryServiceImpl implements TodoQueryService {

    private static final List<String> PROHIBITED_PROPERTIES = List.of("owner");
    private static final Map<String, String> PROPERTY_MAP = new HashMap<>();

    static {
        PROPERTY_MAP.put("id", "todo.id");
        PROPERTY_MAP.put("title", "todo.title");
        PROPERTY_MAP.put("description", "todo.description");
        PROPERTY_MAP.put("deadline", "todo.deadline");
        PROPERTY_MAP.put("completed", "todo.completed");
        PROPERTY_MAP.put("parentId", "todo.parent.id");
        PROPERTY_MAP.put("shared", "todo.shared");
        PROPERTY_MAP.put("priority", "todo.priority.priorityLevel");
        PROPERTY_MAP.put("categories", "todo.categories");
        PROPERTY_MAP.put("createdAt", "todo.createdAt");
        PROPERTY_MAP.put("updatedAt", "todo.updatedAt");
        RSQLJPASupport.addPropertyBlacklist(Todo.class, PROHIBITED_PROPERTIES);
    }

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
            QueryParams<TodoAccess> queryParams = createQueryParams(userId, searchRequest, searchMode);
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

    private QueryParams<TodoAccess> createQueryParams(String userId, SearchRequest searchRequest, SearchMode searchMode) {
        Specification<TodoAccess> query = filterByUserId(userId)
                .and(filterBySearchMode(searchMode));
        Specification<TodoAccess> sort = null;
        if (!StringUtils.isEmpty(searchRequest.getSearch())) {
            query = addSearchSpecification(query, searchRequest.getSearch());
        }
        if (!StringUtils.isEmpty(searchRequest.getSort())) {

            sort = createSort(searchRequest.getSort());
        }
        return QueryParams.<TodoAccess>builder()
                .userId(userId)
                .querySpecification(query)
                .sortSpecification(sort)
                .pageable(PageRequest.of(searchRequest.getPageNumber(), searchRequest.getPageSize()))
                .build();
    }

    private Specification<TodoAccess> filterByUserId(String userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }

    private Specification<TodoAccess> filterBySearchMode(SearchMode searchMode) {
        return (root, query, criteriaBuilder) -> switch (searchMode) {
            case OWN ->
                    criteriaBuilder.equal(root.get("accessLevel").get("accessLevel"), UserAccessLevels.OWNER.getLevel());
            case SHARED ->
                    criteriaBuilder.notEqual(root.get("accessLevel").get("accessLevel"), UserAccessLevels.OWNER.getLevel());
            case ALL -> criteriaBuilder.conjunction();
        };
    }

    private Specification<TodoAccess> addSearchSpecification(Specification<TodoAccess> spec, String search) {
        return spec.and(RSQLJPASupport.toSpecification(search, PROPERTY_MAP));
    }

    private Specification<TodoAccess> createSort(String sort) {
        Specification<TodoAccess> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        return spec.and(RSQLJPASupport.toSort(sort, PROPERTY_MAP));
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
