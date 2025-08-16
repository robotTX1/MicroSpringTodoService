package com.robottx.todoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String search = "";
    private String sort = "id,asc;deadline,desc;createdAt,desc";
    private Integer pageNumber = 0;
    private Integer pageSize = 20;

}
