package com.robottx.todoservice.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoStatistics {

    private Long total;
    private Long finished;
    private Long unfinished;
    private Map<String, TodoStatisticsEntry> statistics;

    public TodoStatistics(Long total, Long finished, Long unfinished) {
        this.total = total;
        this.finished = finished;
        this.unfinished = unfinished;
    }

}
