package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskItemEto;

import java.util.List;
import java.util.Optional;

public interface FindTaskItemUc {

    List<TaskItemEto> findAllTaskItems();

    Optional<TaskItemEto> findTaskItem(Long id);

    List<TaskItemEto> findAllUncompletedOverDueTaskItems();
}
