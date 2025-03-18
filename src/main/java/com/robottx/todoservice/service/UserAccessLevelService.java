package com.robottx.todoservice.service;

import com.robottx.todoservice.entity.UserAccessLevel;

public interface UserAccessLevelService {

    UserAccessLevel findByLevel(Integer level);

}
