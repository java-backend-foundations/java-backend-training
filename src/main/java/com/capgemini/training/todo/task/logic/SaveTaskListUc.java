package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import org.springframework.stereotype.Service;

@Service
public class SaveTaskListUc {

    private final TaskListRepository taskListRepository;

    public SaveTaskListUc(final TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    public void save(final TaskListEto list) {
        taskListRepository.findFirstByName(list.getName())
                .ifPresent((foundTaskList) -> { throw new RuntimeException("TaskList with this title already exists"); });

        TaskListEntity taskList = new TaskListEntity();
        taskList.setId(list.getId());
        taskList.setName(list.getName());

        taskListRepository.save(taskList);
    }
}
