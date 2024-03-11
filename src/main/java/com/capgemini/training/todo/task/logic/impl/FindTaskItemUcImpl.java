package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import com.capgemini.training.todo.task.logic.FindTaskItemUc;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindTaskItemUcImpl implements FindTaskItemUc {

    private final TaskItemRepository taskItemRepository;
    private final TaskItemMapper taskItemMapper;

    public FindTaskItemUcImpl(TaskItemRepository taskItemRepository, TaskItemMapper taskItemMapper) {
        this.taskItemRepository = taskItemRepository;
        this.taskItemMapper = taskItemMapper;
    }

    @Override
    public List<TaskItemEto> findAllTaskItems() {

        return taskItemRepository.findAll().stream().map(taskItemMapper::toTaskItemEto).toList();
    }

    @Override
    public Optional<TaskItemEto> findTaskItem(Long id) {

        return taskItemRepository.findById(id).map(taskItemMapper::toTaskItemEto);
    }

    @Override
    public List<TaskItemEto> findAllUncompletedOverDueTaskItems() {

        return taskItemRepository.findByCompletedAndDeadlineBefore(
                false, Instant.now()).stream().map(taskItemMapper::toTaskItemEto).toList();
    }
}
