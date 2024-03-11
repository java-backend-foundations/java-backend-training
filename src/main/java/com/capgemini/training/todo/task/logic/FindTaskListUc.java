package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;

import java.util.List;
import java.util.Optional;

public interface FindTaskListUc {

    List<TaskListEto> findAllTaskLists();
    Optional<TaskListCto> findTaskList(Long id);

    Optional<TaskListCto> findTaskListByName(String name);
}
