package com.robottx.todoservice.controller;

import static com.robottx.todoservice.constant.EndpointConstants.TODO_STATISTICS_ENDPOINT;

import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.model.TodoStatisticsResponse;
import com.robottx.todoservice.service.todo.TodoStatisticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoStatisticsController {

    private final TodoStatisticsService todoStatisticsService;

    @GetMapping(TODO_STATISTICS_ENDPOINT)
    public ResponseEntity<TodoStatisticsResponse> getBasicStatistics(SearchRequest request,
            @RequestParam(required = false, defaultValue = "OWN") SearchMode mode,
            @RequestParam(required = false, defaultValue = "") String groupBy) {
        return ResponseEntity.ok(todoStatisticsService.getStatistics(request, mode, groupBy));
    }

}
