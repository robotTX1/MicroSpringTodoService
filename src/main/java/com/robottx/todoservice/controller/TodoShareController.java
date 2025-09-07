package com.robottx.todoservice.controller;

import com.robottx.todoservice.model.TodoShareDeleteRequest;
import com.robottx.todoservice.model.TodoShareRequest;
import com.robottx.todoservice.model.TodoShareResponse;
import com.robottx.todoservice.service.todo.TodoShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.robottx.todoservice.constant.EndpointConstants.TODO_SHARE_ENDPOINT;

@RestController
@RequiredArgsConstructor
public class TodoShareController {

    private final TodoShareService todoShareService;

    @GetMapping(TODO_SHARE_ENDPOINT)
    public ResponseEntity<PagedModel<TodoShareResponse>> getTodoShares(@PathVariable Long todoId, Pageable pageable) {
        return ResponseEntity.ok(todoShareService.getTodoShares(todoId, pageable));
    }

    @PutMapping(TODO_SHARE_ENDPOINT)
    public ResponseEntity<Void> shareTodo(@PathVariable Long todoId, @RequestBody @Valid TodoShareRequest request) {
        todoShareService.shareTodo(todoId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(TODO_SHARE_ENDPOINT)
    public ResponseEntity<Void> deleteTodoShare(@PathVariable Long todoId, @Valid TodoShareDeleteRequest request) {
        todoShareService.deleteShare(todoId, request);
        return ResponseEntity.noContent().build();
    }

}
