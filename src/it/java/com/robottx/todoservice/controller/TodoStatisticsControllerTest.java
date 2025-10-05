package com.robottx.todoservice.controller;

import com.robottx.todoservice.MockServerTest;
import com.robottx.todoservice.constant.EndpointConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

class TodoStatisticsControllerTest extends MockServerTest {

    @BeforeEach
    void setUp() {
        mockServer.resetAll();
        createDefaultExpectations();
    }

    @Test
    void getStatisticsForOwnTodos() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=OWN";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_own.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForOwnTodosWithSearch() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=OWN&search=categories.name=='category-1'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_with_category_search_own.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForSharedTodos() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=SHARED";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_shared.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForSharedTodosWithSearch() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=SHARED&search=categories.name=='category-2'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_with_category_search_shared.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForAllTodos() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=ALL";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_all.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForAllTodosWithSearch() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=ALL&search=categories.name=='category-3'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_with_category_search_all.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForOwnTodosGroupedByPriorities() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=OWN&groupBy=priority";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_priority_own.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForOwnTodosGroupedByPrioritiesWithSearch() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT +
                "?mode=OWN&groupBy=priority&search=categories.name=='category-4'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_priority_with_category_search_own.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForSharedTodosGroupedByPriorities() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=SHARED&groupBy=priority";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_priority_shared.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForSharedTodosGroupedByPrioritiesWithSearch() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT +
                "?mode=SHARED&groupBy=priority&search=categories.name=='category-5'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_priority_with_category_search_shared.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForAllTodosGroupedByPriorities() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=ALL&groupBy=priority";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_priority_all.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForAllTodosGroupedByPrioritiesWithSearch() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT +
                "?mode=ALL&groupBy=priority&search=categories.name=='category-1'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_priority_with_category_search_all.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForOwnTodosGroupedByCategories() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=OWN&groupBy=category";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_categories_own.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForOwnTodosGroupedByCategoriesWithSearch() {
        String url =
                EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=OWN&groupBy=category&search=title=ilike='todo'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_categories_with_search_own.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForSharedTodosGroupedByCategories() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=SHARED&groupBy=category";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_categories_shared.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForSharedTodosGroupedByCategoriesWithSearch() {
        String url =
                EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=SHARED&groupBy=category&search=title=ilike='todo'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_categories_with_search_shared.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForAllTodosGroupedByCategories() {
        String url = EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=ALL&groupBy=category";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_categories_all.json",
                HttpStatus.OK
        );
    }

    @Test
    void getStatisticsForAllTodosGroupedByCategoriesWithSearch() {
        String url =
                EndpointConstants.TODO_STATISTICS_ENDPOINT + "?mode=ALL&groupBy=category&search=title=ilike='todo'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "statistics/response/get_statistics_grouped_by_categories_with_search_all.json",
                HttpStatus.OK
        );
    }

}
