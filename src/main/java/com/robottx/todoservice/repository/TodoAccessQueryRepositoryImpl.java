package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.TodoAccess;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoAccessQueryRepositoryImpl implements TodoAccessQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TodoAccess> findAll(Specification<TodoAccess> spec, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Query Shared todos
        CriteriaQuery<TodoAccess> query = criteriaBuilder.createQuery(TodoAccess.class);
        Root<TodoAccess> todoAccessRoot = query.from(TodoAccess.class);
        todoAccessRoot.fetch("todo", JoinType.INNER).fetch("priority", JoinType.INNER);
        todoAccessRoot.fetch("accessLevel", JoinType.INNER);
        Predicate specPredicate = spec != null ? spec.toPredicate(todoAccessRoot, query, criteriaBuilder) : criteriaBuilder.conjunction();
        query.select(todoAccessRoot)
                .where(specPredicate);
        // Apply pagination
        TypedQuery<TodoAccess> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    @Override
    public Long getTotalElements(Specification<TodoAccess> spec) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<TodoAccess> todoAccessRoot = query.from(TodoAccess.class);
        Predicate specPredicate = spec != null ? spec.toPredicate(todoAccessRoot, query, criteriaBuilder) : criteriaBuilder.conjunction();
        query.select(criteriaBuilder.count(todoAccessRoot)).where(specPredicate);
        return entityManager.createQuery(query).getSingleResult();
    }

}
