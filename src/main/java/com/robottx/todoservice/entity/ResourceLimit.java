package com.robottx.todoservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "RESOURCE_LIMIT")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ResourceLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String resource;

    @Column(nullable = false)
    private Integer maxNumber;

}
