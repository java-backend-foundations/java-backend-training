package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskItemEto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface ManageTaskItemUc {

    TaskItemEto saveTaskItem(@Valid TaskItemEto taskItemEto);

    void deleteTaskItem(@NotNull Long id);
}
