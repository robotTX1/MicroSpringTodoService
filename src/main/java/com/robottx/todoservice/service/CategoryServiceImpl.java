package com.robottx.todoservice.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

import com.robottx.todoservice.entity.Category;
import com.robottx.todoservice.entity.TodoWithCategory;
import com.robottx.todoservice.repository.CategoryRepository;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Set<Category> findCategoriesByTodoId(Long todoId) {
        return categoryRepository.findAllByTodoId(todoId);
    }

    @Override
    public Map<Long, Set<Category>> findCategoriesForTodos(Set<Long> todoIds) {
        return categoryRepository.findAllByTodoIds(todoIds).stream()
                .collect(groupingBy(TodoWithCategory::getId,
                        mapping(TodoWithCategory::getCategory, toCollection(LinkedHashSet::new))));
    }

    @Override
    @Transactional
    public Set<Category> createCategories(Set<String> categoryNames) {
        Set<Category> existingCategories = categoryRepository.findAllByNameIn(categoryNames);
        Set<String> categoryNamesToPersist = new LinkedHashSet<>(categoryNames);
        for (Category persistedCategory : existingCategories) {
            categoryNamesToPersist.remove(persistedCategory.getName());
        }
        Set<Category> categoriesToPersist = categoryNamesToPersist.stream()
                .map(c -> new Category(null, c, null, null))
                .collect(toCollection(LinkedHashSet::new));
        Set<Category> persistedCategories = new LinkedHashSet<>(categoryRepository.saveAll(categoriesToPersist));
        persistedCategories.addAll(existingCategories);
        return persistedCategories;
    }

    @Override
    public void deleteCategoryConnections(Long todoId) {
        categoryRepository.deleteAllByTodoId(todoId);
    }

}
