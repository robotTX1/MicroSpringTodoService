package com.robottx.todoservice.service;

import com.robottx.todoservice.entity.Priority;

public interface PriorityService {

    Priority getPriorityByLevel(Integer level);

}
