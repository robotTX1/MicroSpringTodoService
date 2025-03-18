package com.robottx.todoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String search = "";
    private String sort = "deadline,desc;createdAt,desc";
    private Integer pageNumber = 0;
    private Integer pageSize = 20;

}
