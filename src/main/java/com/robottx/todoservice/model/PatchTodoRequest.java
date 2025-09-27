package com.robottx.todoservice.model;

import com.robottx.todoservice.validation.FutureWithDefaultValue;

import java.time.ZonedDateTime;
import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class PatchTodoRequest implements BaseTodoRequest {

    public static final ZonedDateTime DEFAULT_DEADLINE = ZonedDateTime.parse("1970-01-01T00:00:00.000Z");

    @Size(max = 100)
    private String title;

    @Size(max = 4000)
    private String description;

    @FutureWithDefaultValue
    private ZonedDateTime deadline = DEFAULT_DEADLINE;

    private Boolean completed;

    private Long parent;

    @Min(0)
    @Max(4)
    private Integer priority;

    private Set<String> categories;

}
