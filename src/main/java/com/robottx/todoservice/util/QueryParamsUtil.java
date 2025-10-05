package com.robottx.todoservice.util;

import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.domain.UserAccessLevels;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import io.github.perplexhub.rsql.RSQLJPASupport;

@UtilityClass
public class QueryParamsUtil {

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
    }

    public static QueryParams<TodoAccess> createQueryParams(String userId, SearchRequest request,
            SearchMode searchMode) {
        Specification<TodoAccess> query = filterByUserId(userId)
                .and(filterBySearchMode(searchMode));
        Specification<TodoAccess> sort = null;
        if (!StringUtils.isEmpty(request.getSearch())) {
            query = addSearchSpecification(query, request.getSearch());
        }
        if (!StringUtils.isEmpty(request.getSort())) {
            sort = createSort(request.getSort());
        }
        return QueryParams.<TodoAccess>builder()
                .userId(userId)
                .querySpecification(query)
                .sortSpecification(sort)
                .pageable(PageRequest.of(request.getPageNumber(), request.getPageSize()))
                .build();
    }

    public static <T> Specification<T> createSort(String sort) {
        Specification<T> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        return spec.and(RSQLJPASupport.toSort(sort, PROPERTY_MAP));
    }

    private static Specification<TodoAccess> filterByUserId(String userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }

    private static Specification<TodoAccess> filterBySearchMode(SearchMode searchMode) {
        return (root, query, criteriaBuilder) -> switch (searchMode) {
            case OWN -> criteriaBuilder.equal(root.get("accessLevel").get("accessLevel"),
                    UserAccessLevels.OWNER.getLevel());
            case SHARED -> criteriaBuilder.notEqual(root.get("accessLevel").get("accessLevel"),
                    UserAccessLevels.OWNER.getLevel());
            case ALL -> criteriaBuilder.conjunction();
        };
    }

    private static Specification<TodoAccess> addSearchSpecification(Specification<TodoAccess> spec, String search) {
        return spec.and(RSQLJPASupport.toSpecification(search, PROPERTY_MAP));
    }

}
