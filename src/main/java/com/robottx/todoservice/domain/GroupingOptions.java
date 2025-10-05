package com.robottx.todoservice.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupingOptions {
    PRIORITY("priority"),
    CATEGORY("category");

    private final String name;

    public static GroupingOptions fromName(String name) {
        return Arrays.stream(values())
                .filter(groupingOptions -> groupingOptions.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
