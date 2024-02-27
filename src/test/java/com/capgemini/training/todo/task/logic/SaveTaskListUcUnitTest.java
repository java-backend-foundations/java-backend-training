package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveTaskListUcUnitTest {

    @Mock
    private TaskListRepository taskListRepository;

    @Captor
    private ArgumentCaptor<TaskListEntity> captor;

    @InjectMocks
    private SaveTaskListUc saveTaskListUc;

    @Test
    void shouldSaveTaskListWithUniqueName() {
        final String taskListName = "testName";
        final TaskListEto taskListEto = TaskListEto.builder()
                .name(taskListName)
                .build();
        when(taskListRepository.findFirstByName(taskListName))
                .thenReturn(Optional.empty());

        saveTaskListUc.save(taskListEto);

        verify(taskListRepository).save(captor.capture());
        assertEquals(captor.getValue().getName(), taskListName);
    }
}
