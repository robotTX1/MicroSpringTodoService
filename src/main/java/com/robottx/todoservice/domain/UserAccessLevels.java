package com.robottx.todoservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAccessLevels {
    READ(0),
    WRITE(1),
    MANAGE(2),
    OWNER(3);

    private final int level;

}
