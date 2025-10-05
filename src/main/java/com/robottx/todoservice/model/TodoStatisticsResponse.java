package com.robottx.todoservice.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TodoStatisticsResponse {

    private Long total;
    private Long finished;
    private Long unfinished;

    @JsonAnyGetter
    private Map<String, TodoStatisticsEntryResponse> statistics;

}
