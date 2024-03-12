package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import com.capgemini.training.todo.task.logic.ManageTaskListUc;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@Validated
public class ManageTaskListUcImpl implements ManageTaskListUc {

    private final TaskListRepository taskListRepository;

    private final TaskListMapper taskListMapper;

    private final TaskItemMapper taskItemMapper;

    public ManageTaskListUcImpl(TaskListRepository taskListRepository, TaskListMapper taskListMapper, TaskItemMapper taskItemMapper) {
        this.taskListRepository = taskListRepository;
        this.taskListMapper = taskListMapper;
        this.taskItemMapper = taskItemMapper;
    }

    @Override
    public TaskListCto saveTaskList(@Valid TaskListCto taskListCto) {

        TaskListEntity taskListEntity = taskListMapper.toTaskListEntity(taskListCto.taskListEto());
        taskListEntity.setItems(taskListCto.taskItemEtos().stream().map(taskItemMapper::toTaskItemEntity).toList());
        taskListEntity.getItems().forEach(taskItemEntity -> taskItemEntity.setTaskList(taskListEntity));

        TaskListEntity result = taskListRepository.saveAndFlush(taskListEntity);
        return toTaskListCto(result);
    }

    @Override
    public void deleteTaskList(@NotNull Long id) {
        taskListRepository.deleteById(id);
    }

    @Override
    public TaskListCto createTaskListForGivenName(
            @NotNull @Size(min = 5) String taskListName,
            @Min(value = 0) int numberOfItems) {
        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setName(taskListName);

        List<TaskItemEntity> items = IntStream.range(0, numberOfItems).mapToObj(
                i -> createItemForIndex(i, taskListEntity)).toList();
        taskListEntity.setItems(items);

        TaskListEntity result = taskListRepository.saveAndFlush(taskListEntity);
        return toTaskListCto(result);
    }

    private TaskListCto toTaskListCto(TaskListEntity result) {
        return TaskListCto.builder().taskListEto(taskListMapper.toTaskListEto(result))
                .taskItemEtos(result.getItems().stream().map(taskItemMapper::toTaskItemEto).toList())
                .build();
    }

    private TaskItemEntity createItemForIndex(int index, TaskListEntity taskListEntity) {
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName("Task " + (index+1));
        taskItemEntity.setTaskList(taskListEntity);
        return taskItemEntity;
    }
}
