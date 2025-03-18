package com.robottx.todoservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class CreateTodoRequest {

    private String title;
    private String description;
    private ZonedDateTime deadline;

    @NotNull
    private Boolean completed;

    private Long parent;

    @NotNull
    private Integer priority;

    private Set<String> categories;

}
