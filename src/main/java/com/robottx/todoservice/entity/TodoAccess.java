package com.robottx.todoservice.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TODO_USER")
@IdClass(TodoAccess.TodoAccessKey.class)
@EntityListeners(AuditingEntityListener.class)
public class TodoAccess {

    @Id
    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Id
    private String userId;

    @ManyToOne
    @JoinColumn(name = "access_level", nullable = false)
    private UserAccessLevel accessLevel;

    @Data
    public static class TodoAccessKey implements Serializable {

        private Long todo;
        private String userId;

    }

}
