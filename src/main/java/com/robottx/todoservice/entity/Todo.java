package com.robottx.todoservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TODO")
@EntityListeners(AuditingEntityListener.class)
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 4000)
    private String description;

    private ZonedDateTime deadline;

    @Column(nullable = false)
    private Boolean completed;

    @Column(length = 36, nullable = false)
    private String owner;

    @ManyToOne
    @JoinColumn(name = "parent")
    private Todo parent;

    @Column(nullable = false)
    private Boolean shared;

    @ManyToOne
    @JoinColumn(name = "priority", nullable = false)
    private Priority priority;

    @ManyToMany
    @JoinTable(name = "category_todo", joinColumns = {
            @JoinColumn(name = "todo_id")}, inverseJoinColumns = {
            @JoinColumn(name = "category_id")
    })
    private Set<Category> categories;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

}
