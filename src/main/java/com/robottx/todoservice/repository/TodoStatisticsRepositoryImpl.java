package com.robottx.todoservice.repository;

import com.robottx.todoservice.domain.GroupingOptions;
import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.entity.TodoStatistics;
import com.robottx.todoservice.entity.TodoStatisticsEntry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TodoStatisticsRepositoryImpl implements TodoStatisticsRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final TodoAccessQueryRepository todoAccessQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public TodoStatistics basicQuery(QueryParams<TodoAccess> queryParams) {
        List<Long> ids = todoAccessQueryRepository.findAllIdsBySpec(queryParams.getQuerySpecification());
        return basicQuery(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public TodoStatistics queryByGroup(QueryParams<TodoAccess> queryParams, GroupingOptions groupingOption) {
        List<Long> ids = todoAccessQueryRepository.findAllIdsBySpec(queryParams.getQuerySpecification());
        return queryByGroup(ids, groupingOption);
    }

    private TodoStatistics basicQuery(List<Long> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TodoStatistics> query = criteriaBuilder.createQuery(TodoStatistics.class);
        Root<Todo> todoRoot = query.from(Todo.class);
        Predicate queryPredicate = todoRoot.get("id").in(ids);
        Expression<Long> totalExpression = criteriaBuilder.count(todoRoot);
        Expression<Long> finishedExpression = getFinishedExpression(criteriaBuilder, todoRoot);
        Expression<Long> unfinishedExpression = getUnfinishedExpression(criteriaBuilder, todoRoot);
        query.select(criteriaBuilder.construct(
                        TodoStatistics.class,
                        totalExpression,
                        finishedExpression,
                        unfinishedExpression))
                .where(queryPredicate);
        return entityManager.createQuery(query).getSingleResult();
    }

    private TodoStatistics queryByGroup(List<Long> ids, GroupingOptions groupingOption) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = criteriaBuilder.createQuery(Object[].class);
        Root<Todo> todoRoot = query.from(Todo.class);
        Join<Todo, Object> join = joinTableByGroup(todoRoot, groupingOption);
        Predicate queryPredicate = criteriaBuilder.and(
                todoRoot.get("id").in(ids),
                getPredicateByGroup(join, criteriaBuilder, groupingOption)
        );
        Expression<Long> totalExpression = criteriaBuilder.count(todoRoot);
        Expression<Long> finishedExpression = getFinishedExpression(criteriaBuilder, todoRoot);
        Expression<Long> unfinishedExpression = getUnfinishedExpression(criteriaBuilder, todoRoot);
        Path<Object> namePath = getNameByGroup(join, groupingOption);
        Path<Object> groupAndOrderByPath = getGroupAndOrderByPath(join, groupingOption);
        query.multiselect(namePath, totalExpression, finishedExpression, unfinishedExpression)
                .where(queryPredicate)
                .groupBy(groupAndOrderByPath)
                .orderBy(criteriaBuilder.asc(groupAndOrderByPath));
        List<Object[]> todoStatisticsEntryObjects = entityManager.createQuery(query).getResultList();
        return TodoStatistics.builder().statistics(mapTodoStatisticsEntryObjects(todoStatisticsEntryObjects)).build();
    }

    private Expression<Long> getFinishedExpression(CriteriaBuilder criteriaBuilder, Root<Todo> todoRoot) {
        return criteriaBuilder.sum(criteriaBuilder.<Long>selectCase()
                .when(criteriaBuilder.isTrue(todoRoot.get("completed")), 1L).otherwise(0L));
    }

    private Expression<Long> getUnfinishedExpression(CriteriaBuilder criteriaBuilder, Root<Todo> todoRoot) {
        return criteriaBuilder.sum(criteriaBuilder.<Long>selectCase()
                .when(criteriaBuilder.isFalse(todoRoot.get("completed")), 1L).otherwise(0L));
    }

    private Join<Todo, Object> joinTableByGroup(Root<Todo> todoRoot, GroupingOptions groupingOption) {
        return switch (groupingOption) {
            case PRIORITY -> todoRoot.join("priority", JoinType.INNER);
            case CATEGORY -> todoRoot.join("categories", JoinType.LEFT);
        };
    }

    private Path<Object> getNameByGroup(Join<Todo, Object> join, GroupingOptions groupingOption) {
        return switch (groupingOption) {
            case PRIORITY, CATEGORY -> join.get("name");
        };
    }

    private Path<Object> getGroupAndOrderByPath(Join<Todo, Object> join, GroupingOptions groupingOption) {
        return switch (groupingOption) {
            case PRIORITY -> join.get("priorityLevel");
            case CATEGORY -> join.get("name");
        };
    }

    private Predicate getPredicateByGroup(Join<Todo, Object> join, CriteriaBuilder cb, GroupingOptions groupingOption) {
        return switch (groupingOption) {
            case PRIORITY -> cb.conjunction();
            case CATEGORY -> cb.isNotNull(join.get("name"));
        };
    }

    private Map<String, TodoStatisticsEntry> mapTodoStatisticsEntryObjects(List<Object[]> entries) {
        return entries.stream().collect(Collectors.toMap(
                        entry -> (String) entry[0],
                        entry -> new TodoStatisticsEntry((long) entry[1], (long) entry[2], (long) entry[3]),
                        (entryOld, entryNew) -> entryOld,
                        LinkedHashMap::new
                )
        );
    }

}
