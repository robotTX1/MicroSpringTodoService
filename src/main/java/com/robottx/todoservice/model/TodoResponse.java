package com.robottx.todoservice.model;

import java.time.ZonedDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {

    private Long id;
    private String title;
    private String description;
    private ZonedDateTime deadline;
    private Boolean completed;
    private Long parentId;
    private Boolean shared;
    private Integer priority;
    private Set<String> categories;
    private Integer accessLevel;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
