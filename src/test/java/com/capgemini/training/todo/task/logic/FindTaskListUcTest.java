package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FindTaskListUcTest {

    private final static String DEFAULT_TASK_LIST_NAME = "Task List";
    private final static String OTHER_TASK_LIST_NAME = "Other Task List";
    private final static String DEFAULT_TASK_ITEM_NAME = "Task Item";

    @Autowired
    private FindTaskListUc findTaskListUc;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        personRepository.deleteAll();
        taskListRepository.deleteAll();
    }

    @Test
    void findAllTaskLists() {
        // given
        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setName(DEFAULT_TASK_LIST_NAME);
        taskListRepository.save(taskListEntity);

        // when
        List<TaskListEto> result = findTaskListUc.findAllTaskLists();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
    }

    @Test
    void findTaskList() {
        // given
        TaskListEntity taskListEntity = createTaskListWithItem(DEFAULT_TASK_LIST_NAME);

        // when
        Optional<TaskListCto> taskList = findTaskListUc.findTaskList(taskListEntity.getId());

        // then
        assertThat(taskList).isPresent();
        assertThat(taskList.get().taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(taskList.get().taskItemEtos()).hasSize(1);
        assertThat(taskList.get().taskItemEtos().getFirst().name()).isEqualTo(DEFAULT_TASK_ITEM_NAME);
    }

    @Test
    void findTaskListByName() {
        // given
        TaskListEntity taskListEntity = createTaskListWithItem(DEFAULT_TASK_LIST_NAME);
        createTaskListWithItem(OTHER_TASK_LIST_NAME);

        // when
        Optional<TaskListCto> taskList = findTaskListUc.findTaskListByName(taskListEntity.getName());

        // then
        assertThat(taskList).isPresent();
        assertThat(taskList.get().taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);
        assertThat(taskList.get().taskItemEtos()).hasSize(1);
        assertThat(taskList.get().taskItemEtos().getFirst().name()).isEqualTo(DEFAULT_TASK_ITEM_NAME);
    }

    private TaskListEntity createTaskListWithItem(String name) {
        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setName(name);

        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_TASK_ITEM_NAME);
        taskItemEntity.setDeadline(Instant.now());
        taskItemEntity.setTaskList(taskListEntity);

        taskListEntity.setItems(List.of(taskItemEntity));

        taskListEntity = taskListRepository.save(taskListEntity);
        return taskListEntity;
    }
}