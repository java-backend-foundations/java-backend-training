package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskItemMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindTaskItemUcImplTest {

    private final static String DEFAULT_NAME = "Name";

    @InjectMocks
    private FindTaskItemUcImpl findTaskItemUc;

    @Mock
    private TaskItemRepository taskItemRepository;

    @Spy
    private TaskItemMapper taskItemMapper = new TaskItemMapperImpl();

    @Test
    void findAllTaskItems() {
        // given
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_NAME);
        taskItemEntity.setDeadline(Instant.now());

        when(taskItemRepository.findAll()).thenReturn(List.of(taskItemEntity, taskItemEntity));

        // when
        List<TaskItemEto> result = findTaskItemUc.findAllTaskItems();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allSatisfy(taskItemEto -> assertThat(taskItemEto.name()).isEqualTo(DEFAULT_NAME));
        assertThat(result).allSatisfy(taskItemEto -> assertThat(taskItemEto.completed()).isFalse());
    }

    @Test
    void findTaskItem() {
        // given
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_NAME);
        taskItemEntity.setDeadline(Instant.now());

        when(taskItemRepository.findById(1L)).thenReturn(Optional.of(taskItemEntity));

        // when
        Optional<TaskItemEto> result = findTaskItemUc.findTaskItem(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(DEFAULT_NAME);
        assertThat(result.get().completed()).isFalse();
    }

    @Test
    void findAllUncompletedOverDueTaskItems() {
        // given
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_NAME);
        taskItemEntity.setDeadline(Instant.now());

        when(taskItemRepository.findByCompletedAndDeadlineBefore(anyBoolean(), any())).thenReturn(List.of(taskItemEntity));

        // when
        List<TaskItemEto> result = findTaskItemUc.findAllUncompletedOverDueTaskItems();

        // then
        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(taskItemRepository).findByCompletedAndDeadlineBefore(eq(false), instantCaptor.capture());
        // TODO assert instantCaptor.getValue

        assertThat(result).hasSize(1);
        TaskItemEto first = result.getFirst();
        assertThat(first.name()).isEqualTo(DEFAULT_NAME);
    }

}