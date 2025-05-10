package com.robottx.todoservice.model;

import com.robottx.todoservice.validation.ValidTodoTitleOrDescription;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@ValidTodoTitleOrDescription
public class CreateTodoRequest implements BaseTodoRequest {

    @Size(max = 100)
    private String title;

    @Size(max = 4000)
    private String description;

    private ZonedDateTime deadline;

    @NotNull
    private Boolean completed;

    private Long parent;

    @Min(0)
    @Max(4)
    @NotNull
    private Integer priority;

    private Set<String> categories;

}
