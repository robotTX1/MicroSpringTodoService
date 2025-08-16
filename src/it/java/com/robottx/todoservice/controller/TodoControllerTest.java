package com.robottx.todoservice.controller;

import com.robottx.todoservice.MockServerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

class TodoControllerTest extends MockServerTest {

    @BeforeEach
    void setUp() {
        mockServer.resetAll();
        createDefaultExpectations();
    }

    @Test
    void getTodos() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        var response = restTemplate.exchange(EndpointConstants.TODO_ENDPOINT, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/get_all_user1.json");
    }

    @Test
    void getTodosWithHighPriority() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        String url = EndpointConstants.TODO_ENDPOINT + "?search=priority==3";
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/get_all_with_priority_user1.json");
    }

    @Test
    void getTodosWithNoDeadline() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        String url = EndpointConstants.TODO_ENDPOINT + "?search=deadline=null=";
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/get_all_with_no_deadline_user1.json");
    }

    @Test
    void getTodosWithCategory1() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        String url = EndpointConstants.TODO_ENDPOINT + "?search=categories.name=='category-1'";
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/get_all_with_category1_user1.json");
    }

    @Test
    void getTodosWithComplexQuery() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        String query = "title=ilike='todo' and description=inotlike='categories' and deadline=notnull= and priority>1 and categories.name=in=(category-1,category-5)";
        String url = EndpointConstants.TODO_ENDPOINT + "?search=" + query;
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/get_all_with_complex_query_user1.json");
    }

    @Test
    void getTodoById() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(4).toString();
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/get_by_id_user1.json");
    }

    @Test
    void getTodoByIdWhenItDoesNotExists() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(createHeaders());
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(321).toString();
        var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertResponse(response, "todo/response/unauthorized_or_not_found_exception.json", HttpStatus.FORBIDDEN);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loadText("token/user1.jwt"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
