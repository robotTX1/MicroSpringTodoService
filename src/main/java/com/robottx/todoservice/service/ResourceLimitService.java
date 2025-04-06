package com.robottx.todoservice.service;

public interface ResourceLimitService {

    void validateTodoResourceLimit(String userId);

    void validateCategoryLimit(String userId, int count);

    void validateShareLimit(String userId, String otherUserId, Long todoId);

}
