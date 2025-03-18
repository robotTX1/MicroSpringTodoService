package com.robottx.todoservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY_TODO")
@IdClass(CategoryTodo.CategoryTodoKey.class)
@EntityListeners(AuditingEntityListener.class)
public class CategoryTodo {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Id
    @Column(name = "todo_id")
    private Long todoId;

    @Data
    public static class CategoryTodoKey implements Serializable {

        private Long categoryId;
        private Long todoId;

    }

}
