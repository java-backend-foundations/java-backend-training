package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.*;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ManageTaskListUcTest {

    private static final String DEFAULT_TASK_LIST_NAME = "Default Name";

    @Autowired
    private ManageTaskListUc manageTaskListUc;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskItemRepository taskItemRepository;

    @Test
    void saveTaskList() {
        // given
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        List<TaskItemEto> items = List.of(TaskItemEto.builder().name("Task 1").deadline(Instant.now()).build(),
                TaskItemEto.builder().name("Task 2").deadline(Instant.now()).build());
        TaskListCto taskListCto = TaskListCto.builder().taskListEto(taskListEto).taskItemEtos(items).build();

        // when
        TaskListCto result = manageTaskListUc.saveTaskList(taskListCto);

        // then
        assertThat(result.taskListEto()).isNotNull();
        assertThat(result.taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(result.taskItemEtos()).hasSize(2);
    }

    @Test
    public void saveTaskList_shouldNotCreateTaskListWithTooShortName() {
        // given
        TaskListEto taskListEto = TaskListEto.builder().name("xxxx").build();
        TaskListCto taskListCto = TaskListCto.builder().taskListEto(taskListEto).build();

        // when
        assertThatThrownBy(() -> manageTaskListUc.saveTaskList(taskListCto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("saveTaskList.taskListCto.taskListEto.name");
    }

    @Test
    public void saveTaskList_shouldNotCreateTaskListWithInvalidItems() {
        // given
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        List<TaskItemEto> items = List.of(TaskItemEto.builder().name("1").deadline(Instant.now()).build(),
                TaskItemEto.builder().name("2").deadline(Instant.now()).build());
        TaskListCto taskListCto = TaskListCto.builder().taskListEto(taskListEto).taskItemEtos(items).build();

        // when
        assertThatThrownBy(() -> manageTaskListUc.saveTaskList(taskListCto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("saveTaskList.taskListCto.taskItemEtos[0].name");
    }

    @Test
    void deleteTaskList() {
        // given
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        List<TaskItemEto> items = List.of(TaskItemEto.builder().name("Task 1").deadline(Instant.now()).build());
        TaskListCto taskListCto = TaskListCto.builder().taskListEto(taskListEto).taskItemEtos(items).build();
        taskListCto = manageTaskListUc.saveTaskList(taskListCto);

        assertThat(taskListRepository.existsById(taskListCto.taskListEto().id())).isTrue();
        assertThat(taskItemRepository.existsById(taskListCto.taskItemEtos().getFirst().id())).isTrue();

        // when
        manageTaskListUc.deleteTaskList(taskListCto.taskListEto().id());

        // then
        assertThat(taskListRepository.existsById(taskListCto.taskListEto().id())).isFalse();
        assertThat(taskItemRepository.existsById(taskListCto.taskItemEtos().getFirst().id())).isFalse();
    }

    @Test
    void deleteTaskList_shouldValidateIfIdIsNull() {
        // when
        assertThatThrownBy(() -> manageTaskListUc.deleteTaskList(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("deleteTaskList.id");
    }

    @Test
    void createTaskListForGivenName() {
        // given
        // when
        TaskListCto result = manageTaskListUc.createTaskListForGivenName(DEFAULT_TASK_LIST_NAME ,3);

        // then
        assertThat(result.taskListEto()).isNotNull();
        assertThat(result.taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(result.taskItemEtos()).hasSize(3);
    }

    @Test
    void createTaskListForGivenName_shouldNotCreateTaskListWithTooShortName() {
        // when
        assertThatThrownBy(() -> manageTaskListUc.createTaskListForGivenName("xxxx" ,3))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("createTaskListForGivenName.taskListName");
    }

    @Test
    void createTaskListForGivenName_shouldNotCreateTaskListWithNegativeNumberOfItems() {
        // when
        assertThatThrownBy(() -> manageTaskListUc.createTaskListForGivenName(DEFAULT_TASK_LIST_NAME ,-1))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("createTaskListForGivenName.numberOfItems");
    }
}