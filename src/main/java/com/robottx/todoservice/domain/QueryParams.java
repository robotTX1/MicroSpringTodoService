package com.robottx.todoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryParams<T> {

    private String userId;
    private Specification<T> querySpecification;
    private Specification<T> sortSpecification;
    private Pageable pageable;

}
