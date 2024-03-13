package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.PersonCto;
import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageTaskListUcImplTest {

    private static final String DEFAULT_TASK_LIST_NAME = "Default Name";

    @InjectMocks
    private ManageTaskListUcImpl manageTaskListUc;

    @Mock
    private TaskListRepository taskListRepository;

    @Spy
    private TaskListMapper taskListMapper = new TaskListMapperImpl();

    @Spy
    private TaskItemMapper taskItemMapper = new TaskItemMapperImpl();

    @Test
    void saveTaskList() {
        // given
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        List<TaskItemEto> items = List.of(TaskItemEto.builder().name("Task 1").deadline(Instant.now()).build(),
                TaskItemEto.builder().name("Task 2").deadline(Instant.now()).build());
        TaskListCto taskListCto = TaskListCto.builder().taskListEto(taskListEto).taskItemEtos(items).build();

        when(taskListRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);

        // then
        TaskListCto result = manageTaskListUc.saveTaskList(taskListCto);

        // then
        assertThat(result.taskListEto()).isNotNull();
        assertThat(result.taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(result.taskItemEtos()).hasSize(2);

        verify(taskListRepository).saveAndFlush(any());
    }

    @Test
    void deleteTaskList() {
        // given
        // when
        manageTaskListUc.deleteTaskList(1L);

        // then
        verify(taskListRepository).deleteById(1L);
    }

    @Test
    void createTaskListForGivenName() {
        // given
        when(taskListRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        TaskListCto result = manageTaskListUc.createTaskListForGivenName(DEFAULT_TASK_LIST_NAME, 3);

        // then
        assertThat(result.taskListEto()).isNotNull();
        assertThat(result.taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(result.taskItemEtos()).hasSize(3);

        verify(taskListRepository).saveAndFlush(any());
    }
}