package com.robottx.todoservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KeycloakErrorResponse {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

}
