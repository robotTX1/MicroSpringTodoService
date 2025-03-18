package com.robottx.todoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;

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
    private String ownerEmail;
    private Long parentId;
    private Boolean shared;
    private PriorityResponse priority;
    private Set<String> categories;
    private UserAccessLevelResponse accessLevel;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
