package com.robottx.todoservice.service.todo;

import com.oracle.bmc.util.internal.StringUtils;

import com.robottx.todoservice.domain.GroupingOptions;
import com.robottx.todoservice.domain.QueryParams;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.entity.TodoStatistics;
import com.robottx.todoservice.exception.InvalidSearchQueryException;
import com.robottx.todoservice.mapper.TodoStatisticsMapper;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.model.TodoStatisticsResponse;
import com.robottx.todoservice.repository.TodoStatisticsRepository;
import com.robottx.todoservice.security.SecurityService;
import com.robottx.todoservice.util.QueryParamsUtil;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoStatisticsServiceImpl implements TodoStatisticsService {

    private final SecurityService securityService;
    private final TodoStatisticsRepository todoStatisticsRepository;

    @Override
    public TodoStatisticsResponse getStatistics(SearchRequest searchRequest, SearchMode searchMode, String groupBy) {
        // No sorting is required
        searchRequest.setSort("");
        TodoStatistics statistics;
        if (StringUtils.isEmpty(groupBy)) {
            statistics = getBasicStatistics(searchRequest, searchMode);
        } else {
            statistics = getGroupedStatistics(searchRequest, searchMode, groupBy);
        }
        return TodoStatisticsMapper.INSTANCE.domainToModel(statistics);
    }

    private TodoStatistics getBasicStatistics(SearchRequest searchRequest, SearchMode searchMode) {
        String userId = securityService.getId();
        log.debug("Querying basic todo statistics for user {} with search {} and search mode: {}", userId,
                searchRequest, searchMode);
        QueryParams<TodoAccess> queryParams = QueryParamsUtil.createQueryParams(userId, searchRequest, searchMode);
        TodoStatistics statistics = todoStatisticsRepository.basicQuery(queryParams);
        log.debug("Statistics for user {}: {}", userId, statistics);
        return statistics;
    }

    private TodoStatistics getGroupedStatistics(SearchRequest searchRequest, SearchMode searchMode, String groupBy) {
        GroupingOptions groupingOption = getGroupingOption(groupBy);
        String userId = securityService.getId();
        log.debug("Querying grouped todo statistics for user {} with search {} and search mode: {}", userId,
                searchRequest, searchMode);
        QueryParams<TodoAccess> queryParams = QueryParamsUtil.createQueryParams(userId, searchRequest, searchMode);
        TodoStatistics statistics = todoStatisticsRepository.queryByGroup(queryParams, groupingOption);
        log.debug("Grouped statistics for user {}: {}", userId, statistics);
        return statistics;
    }

    private GroupingOptions getGroupingOption(String groupBy) {
        return Optional
                .ofNullable(GroupingOptions.fromName(groupBy))
                .orElseThrow(() -> new InvalidSearchQueryException("Invalid grouping option: " + groupBy));
    }

}
