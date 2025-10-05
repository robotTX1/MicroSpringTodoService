package com.robottx.todoservice.repository;

import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.entity.TodoAccess;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TodoAccessQueryRepositoryImpl implements TodoAccessQueryRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<TodoAccess> findAll(QueryParams<TodoAccess> queryParams) {
        List<Long> ids = findAllIdsBySpec(queryParams.getQuerySpecification());
        List<TodoAccess> todoAccesses = findAllById(ids, queryParams);
        return new PageImpl<>(todoAccesses, queryParams.getPageable(), ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllIdsBySpec(Specification<TodoAccess> spec) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<TodoAccess> todoAccessRoot = query.from(TodoAccess.class);
        Join<TodoAccess, Todo> todo = todoAccessRoot.join("todo", JoinType.INNER);
        todo.join("priority", JoinType.INNER);
        todo.join("categories", JoinType.LEFT);
        todoAccessRoot.join("accessLevel", JoinType.INNER);
        Predicate specPredicate =
                spec != null ? spec.toPredicate(todoAccessRoot, query, criteriaBuilder) : criteriaBuilder.conjunction();
        query.select(todoAccessRoot.get("todo").get("id"))
                .distinct(true)
                .where(specPredicate);
        return entityManager.createQuery(query).getResultList();
    }

    private List<TodoAccess> findAllById(List<Long> ids, QueryParams<TodoAccess> queryParams) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TodoAccess> query = criteriaBuilder.createQuery(TodoAccess.class);
        Root<TodoAccess> todoAccessRoot = query.from(TodoAccess.class);
        todoAccessRoot.fetch("todo", JoinType.INNER).fetch("priority", JoinType.INNER);
        todoAccessRoot.fetch("accessLevel", JoinType.INNER);
        Predicate queryPredicate = criteriaBuilder.and(
                criteriaBuilder.equal(todoAccessRoot.get("userId"), queryParams.getUserId()),
                todoAccessRoot.get("todo").get("id").in(ids)
        );
        if (queryParams.getSortSpecification() != null) {
            Predicate sortPredicate = queryParams.getSortSpecification()
                    .toPredicate(todoAccessRoot, query, criteriaBuilder);
            queryPredicate = criteriaBuilder.and(queryPredicate, sortPredicate);
        }
        query.select(todoAccessRoot)
                .where(queryPredicate);
        Pageable pageable = queryParams.getPageable();
        TypedQuery<TodoAccess> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

}
