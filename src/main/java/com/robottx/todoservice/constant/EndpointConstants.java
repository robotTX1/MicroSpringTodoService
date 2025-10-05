package com.robottx.todoservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EndpointConstants {

    public static final String API_V1 = "/api/v1";

    public static final String TODO_ENDPOINT = API_V1 + "/todos";
    public static final String TODO_BY_ID_ENDPOINT = TODO_ENDPOINT + "/{todoId}";
    public static final String TODO_SHARE_ENDPOINT = TODO_BY_ID_ENDPOINT + "/share";
    public static final String TODO_STATISTICS_ENDPOINT = TODO_ENDPOINT + "/statistics";

}
