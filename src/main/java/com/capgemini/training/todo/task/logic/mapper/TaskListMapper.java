package com.capgemini.training.todo.task.logic.mapper;

import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskListMapper {

    TaskListEto toTaskListEto(TaskListEntity taskListEntity);

    TaskListEntity toTaskListEntity(TaskListEto taskListEto);
}
