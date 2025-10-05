package com.robottx.todoservice.service.todo;

import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.model.TodoStatisticsResponse;

public interface TodoStatisticsService {

    TodoStatisticsResponse getStatistics(SearchRequest searchRequest, SearchMode searchMode, String groupBy);

}
