package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import com.capgemini.training.todo.task.logic.ManageTaskItemUc;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
public class ManageTaskItemUcImpl implements ManageTaskItemUc {

    private final TaskItemRepository taskItemRepository;

    private final TaskItemMapper taskItemMapper;

    public ManageTaskItemUcImpl(TaskItemRepository taskItemRepository, TaskItemMapper taskItemMapper) {
        this.taskItemRepository = taskItemRepository;
        this.taskItemMapper = taskItemMapper;
    }

    @Override
    @PreAuthorize("hasRole(@Roles.ROLE_MAINTAINER)")
    public TaskItemEto saveTaskItem(@Valid TaskItemEto taskItemEto) {

        TaskItemEntity taskItemEntity = taskItemMapper.toTaskItemEntity(taskItemEto);
        taskItemEntity = taskItemRepository.saveAndFlush(taskItemEntity);
        return taskItemMapper.toTaskItemEto(taskItemEntity);
    }

    @Override
    @PreAuthorize("hasRole(@Roles.ROLE_MAINTAINER)")
    public void deleteTaskItem(@NotNull Long id) {

        taskItemRepository.deleteById(id);
    }
}
