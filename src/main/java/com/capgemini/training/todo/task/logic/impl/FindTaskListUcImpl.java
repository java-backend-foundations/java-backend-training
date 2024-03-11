package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import com.capgemini.training.todo.task.logic.FindTaskListUc;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindTaskListUcImpl implements FindTaskListUc {

    private final TaskListRepository taskListRepository;

    private final TaskListMapper taskListMapper;

    private final TaskItemMapper taskItemMapper;

    public FindTaskListUcImpl(TaskListRepository taskListRepository, TaskListMapper taskListMapper, TaskItemMapper taskItemMapper) {
        this.taskListRepository = taskListRepository;
        this.taskListMapper = taskListMapper;
        this.taskItemMapper = taskItemMapper;
    }

    @Override
    public List<TaskListEto> findAllTaskLists() {
        return taskListRepository.findAll().stream().map(taskListMapper::toTaskListEto).toList();
    }

    @Override
    public Optional<TaskListCto> findTaskList(Long id) {
        return taskListRepository.findById(id).map(this::toTaskListCto);
    }

    @Override
    public Optional<TaskListCto> findTaskListByName(String name) {
        return taskListRepository.findFirstByName(name).map(this::toTaskListCto);
    }

    private TaskListCto toTaskListCto(TaskListEntity taskListEntity) {
        return TaskListCto.builder()
                .taskListEto(taskListMapper.toTaskListEto(taskListEntity))
                .taskItemEtos(taskListEntity.getItems().stream().map(taskItemMapper::toTaskItemEto).toList())
                .build();
    }
}
