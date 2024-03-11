package com.capgemini.training.todo.task.logic.mapper;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskItemMapper {

    TaskItemEto toTaskItemEto(TaskItemEntity taskItemEntity);

    TaskItemEntity toTaskItemEntity(TaskItemEto taskItemEto);
}
