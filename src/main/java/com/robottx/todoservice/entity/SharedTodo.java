package com.robottx.todoservice.entity;

public record SharedTodo(Todo todo, UserAccessLevel accessLevel) {
}
