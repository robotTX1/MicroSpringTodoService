package com.robottx.todoservice.controller;

import com.robottx.todoservice.MockServerTest;
import com.robottx.todoservice.entity.TodoAccess;
import com.robottx.todoservice.repository.TodoAccessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TodoControllerTest extends MockServerTest {

    @Autowired
    TodoAccessRepository todoAccessRepository;

    @BeforeEach
    void setUp() {
        mockServer.resetAll();
        createDefaultExpectations();
    }

    @Test
    void getOwnTodos() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_own_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getSharedTodos() {
        String url = EndpointConstants.TODO_ENDPOINT + "?mode=SHARED";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_shared_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getAllTodos() {
        String url = EndpointConstants.TODO_ENDPOINT + "?mode=ALL";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_all_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodosWithHighPriority() {
        String url = EndpointConstants.TODO_ENDPOINT + "?search=priority==3";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_own_with_priority_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodosWithNoDeadline() {
        String url = EndpointConstants.TODO_ENDPOINT + "?search=deadline=null=";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_own_with_no_deadline_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodosWithCategory1() {
        String url = EndpointConstants.TODO_ENDPOINT + "?search=categories.name=='category-1'";
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_own_with_category1_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodosWithComplexQuery() {
        String query = "title=ilike='todo' and description=inotlike='categories' and deadline=notnull= and priority>1 and categories.name=in=(category-1,category-5)";
        String url = EndpointConstants.TODO_ENDPOINT + "?search=" + query;
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_own_with_complex_query_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodoById() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(4).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/get_by_id_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodoByIdWhenItDoesNotExists() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(321).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void postTodo() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_valid_user1.json",
                "todo/response/post_valid_user1.json",
                HttpStatus.CREATED
        );
    }

    @Test
    void postTodoWithEmptyTitleAndDescription() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_with_invalid_title_and_description_user1.json",
                "todo/response/post_with_invalid_title_and_description_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void postTodoWithInvalidDeadline() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_with_invalid_deadline_user1.json",
                "todo/response/post_with_invalid_deadline_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void postTodoWithEmptyCompletedField() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_with_empty_completed_user1.json",
                "todo/response/post_with_empty_completed_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void postTodoWithInvalidParent() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_with_invalid_parent_user1.json",
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    void postTodoWithInvalidPriority() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_with_invalid_priority_user1.json",
                "todo/response/post_with_invalid_priority_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void postTodoWithMoreThan10Categories() {
        testEndpointWithUser1(
                EndpointConstants.TODO_ENDPOINT,
                HttpMethod.POST,
                "todo/request/post_with_more_than_ten_categories_user1.json",
                "todo/response/post_with_more_than_ten_categories_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void putTodo() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_valid_user1.json",
                "todo/response/put_valid_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void putSharedTodoWithPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(8).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_valid_shared_user1.json",
                "todo/response/put_valid_shared_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void putTodoWithEmptyTitleAndDescription() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_with_invalid_title_and_description_user1.json",
                "todo/response/put_with_invalid_title_and_description_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void putTodoWithInvalidDeadline() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_with_invalid_deadline_user1.json",
                "todo/response/put_with_invalid_deadline_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void putTodoWithEmptyCompletedField() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_with_empty_completed_user1.json",
                "todo/response/put_with_empty_completed_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void putTodoWithInvalidParent() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_with_invalid_parent_user1.json",
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    void putTodoWithInvalidPriority() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_with_invalid_priority_user1.json",
                "todo/response/put_with_invalid_priority_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void putTodoWithMoreThan10Categories() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_with_more_than_10_categories_user1.json",
                "todo/response/put_with_more_than_10_categories_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void putTodoWithoutPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(6).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "todo/request/put_valid_shared_user1.json",
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void patchTodoWithSingleField() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_valid_single_user1.json",
                "todo/response/patch_valid_single_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void patchTodoWithMultipleField() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_valid_multiple_user1.json",
                "todo/response/patch_valid_multiple_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void patchTodoWithPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(8).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_valid_with_privileges_user1.json",
                "todo/response/patch_valid_with_privileges_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void patchTodoWithInvalidTitleAndDescription() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_with_invalid_title_and_description_user1.json",
                "todo/response/patch_with_invalid_title_and_description_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void patchTodoWithNullDeadline() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_with_null_deadline_user1.json",
                "todo/response/patch_with_null_deadline_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void patchTodoWithInvalidDeadline() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_with_invalid_deadline_user1.json",
                "todo/response/patch_with_invalid_deadline_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void patchTodoToRemoveParent() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(3).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_remove_parent_user1.json",
                "todo/response/patch_remove_parent_user1.json",
                HttpStatus.OK
        );
    }

    @Test
    void patchTodoWithInvalidParent() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_with_invalid_parent_user1.json",
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    void patchTodoWithInvalidPriority() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_with_invalid_priority_user1.json",
                "todo/response/patch_with_invalid_priority_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void patchTodoWithMoreThan10Categories() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_with_more_than_10_categories_user1.json",
                "todo/response/patch_with_more_than_10_categories_user1.json",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void patchTodoWithoutPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(7).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PATCH,
                "todo/request/patch_valid_with_privileges_user1.json",
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteTodo() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                NO_RESPONSE_BODY,
                HttpStatus.NO_CONTENT
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId("testuser1", 1L);
        assertTrue(todoAccessOptional.isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteTodoWithPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(9).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                NO_RESPONSE_BODY,
                HttpStatus.NO_CONTENT
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId("testuser1", 9L);
        assertTrue(todoAccessOptional.isEmpty());
    }

    @Test
    void deleteTodoWithoutPermission() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_BY_ID_ENDPOINT)
                .buildAndExpand(6).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                "todo/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

}
