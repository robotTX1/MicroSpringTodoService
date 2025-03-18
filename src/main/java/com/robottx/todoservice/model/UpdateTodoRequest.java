package com.robottx.todoservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class UpdateTodoRequest {

    public static final ZonedDateTime DEFAULT_DEADLINE = ZonedDateTime.parse("1970-01-01T00:00:00.000Z");

    private String title;
    private String description;
    private ZonedDateTime deadline = DEFAULT_DEADLINE;

    @NotNull
    private Boolean completed;

    private Long parent;

    @NotNull
    private Integer priority;

    private Set<String> categories;

}
