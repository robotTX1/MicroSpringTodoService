package com.robottx.todoservice.mapper;

import com.robottx.todoservice.entity.Category;
import com.robottx.todoservice.entity.Priority;
import com.robottx.todoservice.entity.Todo;
import com.robottx.todoservice.model.PriorityResponse;
import com.robottx.todoservice.model.TodoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TodoMapper {

    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    @Mapping(source = "parent.id", target = "parentId")
    TodoResponse domainToModel(Todo todo);

    PriorityResponse domainToModel(Priority priority);

    default String domainToModel(Category category) {
        return category == null ? null : category.getName();
    }

}
