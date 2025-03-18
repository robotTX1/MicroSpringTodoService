package com.robottx.todoservice.service;

import com.robottx.todoservice.entity.Category;

import java.util.Map;
import java.util.Set;

public interface CategoryService {

    Set<Category> findCategoriesByTodoId(Long todoId);

    Map<Long, Set<Category>> findCategoriesForTodos(Set<Long> todoIds);

    Set<Category> createCategories(Set<String> categories);

    void deleteCategoryConnections(Long todoId);

}
