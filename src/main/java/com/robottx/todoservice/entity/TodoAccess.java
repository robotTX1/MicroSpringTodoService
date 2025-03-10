package com.robottx.todoservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@Entity
@Builder
@IdClass(TodoAccess.TodoAccessKey.class)
@Table(name = "TODO_USER")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoAccess {

    @Id
    private Long todoId;

    @Id
    private String userId;

    @ManyToOne
    @JoinColumn(name = "access_level", nullable = false)
    private UserAccessLevel accessLevel;

    @Data
    public static class TodoAccessKey implements Serializable {

        private Long todoId;
        private String userId;

    }

}
