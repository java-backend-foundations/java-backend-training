package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskListCto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public interface ManageTaskListUc {

    TaskListCto saveTaskList(@Valid TaskListCto taskListCto);
    void deleteTaskList(@NotNull Long id);

    TaskListCto createTaskListForGivenName(@NotNull @Size(min = 5) String taskListName,
                                           @Min(value = 0) int numberOfItems);
}
