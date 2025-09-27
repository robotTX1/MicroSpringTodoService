package com.robottx.todoservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

@Data
@Entity
@Builder
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRIORITY")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Priority {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer priorityLevel;

    @Column(length = 30, nullable = false)
    private String name;

}
