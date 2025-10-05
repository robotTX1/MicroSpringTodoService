package com.robottx.todoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoStatisticsEntryResponse {

    private Long total;
    private Long finished;
    private Long unfinished;

}
