package com.robottx.todoservice.controller;

import com.robottx.todoservice.model.CreateTodoRequest;
import com.robottx.todoservice.model.SearchMode;
import com.robottx.todoservice.model.SearchRequest;
import com.robottx.todoservice.model.TodoResponse;
import com.robottx.todoservice.model.UpdateTodoRequest;
import com.robottx.todoservice.service.todo.TodoServiceFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.robottx.todoservice.controller.EndpointConstants.TODO_BY_ID_ENDPOINT;
import static com.robottx.todoservice.controller.EndpointConstants.TODO_ENDPOINT;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoServiceFacade todoServiceFacade;

    @GetMapping(TODO_ENDPOINT)
    public ResponseEntity<PagedModel<TodoResponse>> getTodos(SearchRequest request,
                                                             @RequestParam(required = false, defaultValue = "OWN") SearchMode mode) {
        return ResponseEntity.ok(todoServiceFacade.findAllTodos(request, mode));
    }

    @GetMapping(TODO_BY_ID_ENDPOINT)
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long todoId) {
        return ResponseEntity.ok(todoServiceFacade.findTodoById(todoId));
    }

    @PostMapping(TODO_ENDPOINT)
    public ResponseEntity<TodoResponse> createTodo(@RequestBody @Valid CreateTodoRequest request) {
        TodoResponse response = todoServiceFacade.createTodo(request);
        return ResponseEntity.created(buildUri(response.getId())).body(response);
    }

    @PutMapping(TODO_BY_ID_ENDPOINT)
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long todoId, @RequestBody @Valid UpdateTodoRequest request) {
        return ResponseEntity.ok(todoServiceFacade.updateTodo(todoId, request));
    }

    @PatchMapping(TODO_BY_ID_ENDPOINT)
    public ResponseEntity<TodoResponse> patchTodo(@PathVariable Long todoId, @RequestBody UpdateTodoRequest request) {
        return ResponseEntity.ok(todoServiceFacade.patchTodo(todoId, request));
    }

    @DeleteMapping(TODO_BY_ID_ENDPOINT)
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
        todoServiceFacade.deleteTodo(todoId);
        return ResponseEntity.noContent().build();
    }

    private URI buildUri(Long todoId) {
        return UriComponentsBuilder.fromUriString(TODO_BY_ID_ENDPOINT)
                .buildAndExpand(todoId)
                .toUri();
    }

}
