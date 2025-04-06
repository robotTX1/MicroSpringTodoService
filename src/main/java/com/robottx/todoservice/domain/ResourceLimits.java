package com.robottx.todoservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResourceLimits {
    TODO_LIMIT("TODO_LIMIT"),
    CATEGORY_LIMIT("CATEGORY_LIMIT_PER_TODO"),
    SHARE_LIMIT("SHARE_LIMIT_PER_TODO");

    private final String name;

}
