package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageTaskItemUcImplTest {

    private static final String DEFAULT_NAME = "Default";

    @InjectMocks
    private ManageTaskItemUcImpl manageTaskItemUc;

    @Mock
    private TaskItemRepository taskItemRepository;

    @Spy
    private TaskItemMapper taskItemMapper = new TaskItemMapperImpl();

    @Test
    void saveTaskItem() {
        // given
        TaskItemEto taskItemEto = TaskItemEto.builder().name(DEFAULT_NAME).deadline(Instant.now()).build();
        when(taskItemRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        TaskItemEto result = manageTaskItemUc.saveTaskItem(taskItemEto);

        // then
        ArgumentCaptor<TaskItemEntity> personEntityCaptor = ArgumentCaptor.forClass(TaskItemEntity.class);
        verify(taskItemRepository).saveAndFlush(personEntityCaptor.capture());
        assertThat(personEntityCaptor.getValue().getName()).isEqualTo(DEFAULT_NAME);

        assertThat(result.name()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void deleteTaskItem() {
        // given
        // when
        manageTaskItemUc.deleteTaskItem(1L);

        // then
        verify(taskItemRepository).deleteById(1L);
    }
}