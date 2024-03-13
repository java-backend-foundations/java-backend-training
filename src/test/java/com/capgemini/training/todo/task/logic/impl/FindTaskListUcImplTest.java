package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapperImpl;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTaskListUcImplTest {

    private final static String DEFAULT_TASK_LIST_NAME = "Task List";
    private final static String OTHER_TASK_LIST_NAME = "Other Task List";
    private final static String DEFAULT_TASK_ITEM_NAME = "Task Item";

    @InjectMocks
    private FindTaskListUcImpl findTaskListUc;

    @Mock
    private TaskListRepository taskListRepository;

    @Spy
    private TaskItemMapper taskItemMapper = new TaskItemMapperImpl();

    @Spy
    private TaskListMapper taskListMapper = new TaskListMapperImpl();

    @Test
    void findAllTaskLists() {
        // given
        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setName(DEFAULT_TASK_LIST_NAME);

        when(taskListRepository.findAll()).thenReturn(List.of(taskListEntity));

        // when
        List<TaskListEto> result = findTaskListUc.findAllTaskLists();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
    }

    @Test
    void findTaskList() {
        // given
        when(taskListRepository.findById(1L)).thenReturn(Optional.of(createTaskListWithItem()));

        // when
        Optional<TaskListCto> taskList = findTaskListUc.findTaskList(1L);

        // then
        assertThat(taskList).isPresent();
        assertThat(taskList.get().taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(taskList.get().taskItemEtos()).hasSize(1);
        assertThat(taskList.get().taskItemEtos().getFirst().name()).isEqualTo(DEFAULT_TASK_ITEM_NAME);
    }

    @Test
    void findTaskListByName() {
        // given
        when(taskListRepository.findFirstByName("test")).thenReturn(Optional.of(createTaskListWithItem()));

        // when
        Optional<TaskListCto> taskList = findTaskListUc.findTaskListByName("test");

        // then
        assertThat(taskList).isPresent();
        assertThat(taskList.get().taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(taskList.get().taskItemEtos()).hasSize(1);
        assertThat(taskList.get().taskItemEtos().getFirst().name()).isEqualTo(DEFAULT_TASK_ITEM_NAME);
    }

    private TaskListEntity createTaskListWithItem() {
        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setName(DEFAULT_TASK_LIST_NAME);

        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_TASK_ITEM_NAME);
        taskItemEntity.setDeadline(Instant.now());
        taskItemEntity.setTaskList(taskListEntity);

        taskListEntity.setItems(List.of(taskItemEntity));

        return taskListEntity;
    }
}