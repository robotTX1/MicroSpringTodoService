package com.robottx.todoservice.controller;

import com.robottx.todoservice.model.TodoShareDeleteRequest;
import com.robottx.todoservice.model.TodoShareRequest;
import com.robottx.todoservice.service.todo.TodoShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.robottx.todoservice.controller.EndpointConstants.TODO_SHARE_ENDPOINT;

@RestController
@RequiredArgsConstructor
public class TodoShareController {

    private final TodoShareService todoShareService;

    @PostMapping(TODO_SHARE_ENDPOINT)
    public ResponseEntity<Void> shareTodo(@PathVariable Long todoId, @RequestBody @Valid TodoShareRequest request) {
        todoShareService.shareTodo(todoId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(TODO_SHARE_ENDPOINT)
    public ResponseEntity<Void> deleteTodoShare(@PathVariable Long todoId, @RequestBody @Valid TodoShareDeleteRequest request) {
        todoShareService.deleteShare(todoId, request);
        return ResponseEntity.noContent().build();
    }

}
