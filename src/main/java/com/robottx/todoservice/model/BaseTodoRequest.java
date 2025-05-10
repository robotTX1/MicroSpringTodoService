package com.robottx.todoservice.model;

import java.time.ZonedDateTime;
import java.util.Set;

public interface BaseTodoRequest {

    String getTitle();

    String getDescription();

    ZonedDateTime getDeadline();

    Boolean getCompleted();

    Long getParent();

    Integer getPriority();

    Set<String> getCategories();

}
