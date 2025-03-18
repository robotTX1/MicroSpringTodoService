package com.robottx.todoservice.mapper;

import com.robottx.todoservice.entity.UserAccessLevel;
import com.robottx.todoservice.model.UserAccessLevelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAccessLevelMapper {

    UserAccessLevelMapper INSTANCE = Mappers.getMapper(UserAccessLevelMapper.class);

    UserAccessLevelResponse domainToModel(UserAccessLevel userAccessLevel);

}
