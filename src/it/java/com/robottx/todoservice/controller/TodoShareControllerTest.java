package com.robottx.todoservice.controller;

import com.robottx.todoservice.MockServerTest;
import com.robottx.todoservice.constant.EndpointConstants;
import com.robottx.todoservice.domain.UserAccessLevels;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TodoShareControllerTest extends MockServerTest {

    @Autowired
    TodoAccessRepository todoAccessRepository;

    @BeforeEach
    void setUp() {
        mockServer.resetAll();
        createDefaultExpectations();
    }

    @Test
    void getTodoShares() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(7).toString();
        testEndpointWithUser2(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "share/response/get_with_valid_access.json",
                HttpStatus.OK
        );
    }

    @Test
    void getTodoSharesWithoutPrivilege() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(1).toString();
        testEndpointWithUser2(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "share/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    void getTodoSharesWithNonExistingId() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(0).toString();
        testEndpointWithUser2(
                url,
                HttpMethod.GET,
                NO_REQUEST_BODY,
                "share/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void putTodoShare() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(6).toString();
        testEndpointWithUser2(
                url,
                HttpMethod.PUT,
                "share/request/put_valid_todo_share.json",
                NO_RESPONSE_BODY,
                HttpStatus.OK
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER1_ID, 6L);
        assertTrue(todoAccessOptional.isPresent());
        TodoAccess todoAccess = todoAccessOptional.get();
        assertEquals(UserAccessLevels.READ.getLevel(), todoAccess.getAccessLevel().getAccessLevel());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void putTodoShareDecreaseOwnAccess() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(9).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "share/request/put_valid_todo_share_decrease_access.json",
                NO_RESPONSE_BODY,
                HttpStatus.OK
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER1_ID, 9L);
        assertTrue(todoAccessOptional.isPresent());
        TodoAccess todoAccess = todoAccessOptional.get();
        assertEquals(UserAccessLevels.READ.getLevel(), todoAccess.getAccessLevel().getAccessLevel());
    }

    @Test
    void putTodoShareWithoutPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(8).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "share/request/put_valid_todo_share_without_privilege.json",
                "share/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER1_ID, 8L);
        assertTrue(todoAccessOptional.isPresent());
        TodoAccess todoAccess = todoAccessOptional.get();
        assertEquals(UserAccessLevels.WRITE.getLevel(), todoAccess.getAccessLevel().getAccessLevel());
    }

    @Test
    void putTodoShareDecreaseOwnerAccessLevel() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .buildAndExpand(9).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.PUT,
                "share/request/put_valid_todo_share_decrease_owner_access_level.json",
                "share/response/modify_owner_exception.json",
                HttpStatus.FORBIDDEN
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER2_ID, 9L);
        assertTrue(todoAccessOptional.isPresent());
        TodoAccess todoAccess = todoAccessOptional.get();
        assertEquals(UserAccessLevels.OWNER.getLevel(), todoAccess.getAccessLevel().getAccessLevel());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteTodoShare() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .queryParam("email", TEST_USER1_EMAIL)
                .buildAndExpand(7).toString();
        testEndpointWithUser2(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                NO_RESPONSE_BODY,
                HttpStatus.NO_CONTENT
        );
        Optional<TodoAccess> user1TodoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER1_ID, 7L);
        assertTrue(user1TodoAccessOptional.isEmpty());
        Optional<TodoAccess> user2TodoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER2_ID, 7L);
        assertTrue(user2TodoAccessOptional.isPresent());
        TodoAccess user2TodoAccess = user2TodoAccessOptional.get();
        assertFalse(user2TodoAccess.getTodo().getShared());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteSelfTodoShare() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .queryParam("email", TEST_USER1_EMAIL)
                .buildAndExpand(7).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                NO_RESPONSE_BODY,
                HttpStatus.NO_CONTENT
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER1_ID, 7L);
        assertTrue(todoAccessOptional.isEmpty());
    }

    @Test
    void tryDeleteTodoShareOwner() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .queryParam("email", TEST_USER2_EMAIL)
                .buildAndExpand(7).toString();
        testEndpointWithUser2(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                "share/response/modify_owner_exception.json",
                HttpStatus.FORBIDDEN
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER2_ID, 7L);
        assertTrue(todoAccessOptional.isPresent());
    }

    @Test
    void tryDeleteTodoShareOwnerByOtherUser() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .queryParam("email", TEST_USER2_EMAIL)
                .buildAndExpand(9).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                "share/response/modify_owner_exception.json",
                HttpStatus.FORBIDDEN
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER2_ID, 9L);
        assertTrue(todoAccessOptional.isPresent());
    }

    @Test
    void tryDeleteTodoShareOwnerByOtherUserWithoutPrivileges() {
        String url = UriComponentsBuilder
                .fromUriString(EndpointConstants.TODO_SHARE_ENDPOINT)
                .queryParam("email", TEST_USER2_EMAIL)
                .buildAndExpand(7).toString();
        testEndpointWithUser1(
                url,
                HttpMethod.DELETE,
                NO_REQUEST_BODY,
                "share/response/unauthorized_or_not_found_exception.json",
                HttpStatus.FORBIDDEN
        );
        Optional<TodoAccess> todoAccessOptional = todoAccessRepository.findByUserIdAndTodoId(TEST_USER2_ID, 7L);
        assertTrue(todoAccessOptional.isPresent());
    }

}
