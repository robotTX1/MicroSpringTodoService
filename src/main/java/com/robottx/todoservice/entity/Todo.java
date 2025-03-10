package com.robottx.todoservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.YesNoConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "TODO")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Lob
    private String description;

    private ZonedDateTime deadline;

    @Column(nullable = false)
    @Convert(converter = YesNoConverter.class)
    private Boolean completed;

    @Column(length = 36, nullable = false)
    private String owner;

    @ManyToOne
    @JoinColumn(name = "parent")
    private Todo parent;

    @Column(nullable = false)
    @Convert(converter = YesNoConverter.class)
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
