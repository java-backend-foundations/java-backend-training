package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindTaskListUc {

    private final TaskListRepository taskListRepository;

    public FindTaskListUc(final TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    public TaskListEto findById(Long listId) {
        Optional<TaskListEntity> taskList = this.taskListRepository.findById(listId);
        return taskList.map(this::convert).orElse(null);
    }
    
    private TaskListEto convert(final TaskListEntity entity) {
        return TaskListEto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
