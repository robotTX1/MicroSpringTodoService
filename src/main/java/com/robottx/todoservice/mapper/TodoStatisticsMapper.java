package com.robottx.todoservice.mapper;

import com.robottx.todoservice.entity.TodoStatistics;
import com.robottx.todoservice.model.TodoStatisticsResponse;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TodoStatisticsMapper {

    TodoStatisticsMapper INSTANCE = Mappers.getMapper(TodoStatisticsMapper.class);

    TodoStatisticsResponse domainToModel(TodoStatistics statistics);

}
