package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FindTaskItemUcTest {

    private final static String DEFAULT_NAME = "Name";

    @Autowired
    private FindTaskItemUc findTaskItemUc;

    @Autowired
    private TaskItemRepository taskItemRepository;

    @BeforeEach
    void setup() {
        taskItemRepository.deleteAll();
    }

    @Test
    void findAllTaskItems() {
        // given
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_NAME);
        taskItemEntity.setDeadline(Instant.now());
        taskItemRepository.save(taskItemEntity);

        // when
        List<TaskItemEto> result = findTaskItemUc.findAllTaskItems();

        // then
        assertThat(result).hasSize(1);
        TaskItemEto first = result.getFirst();
        assertThat(first.name()).isEqualTo(DEFAULT_NAME);
        assertThat(first.completed()).isFalse();
    }

    @Test
    void findTaskItem() {
        // given
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_NAME);
        taskItemEntity.setDeadline(Instant.now());
        taskItemEntity = taskItemRepository.save(taskItemEntity);

        // when
        Optional<TaskItemEto> result = findTaskItemUc.findTaskItem(taskItemEntity.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(DEFAULT_NAME);
        assertThat(result.get().completed()).isFalse();
    }

    @Test
    void findAllUncompletedOverDueTaskItems() {
        // given
        TaskItemEntity expected = createTaskItemEntity(Instant.parse("2002-05-05T10:10:00.00Z"), false);
        createTaskItemEntity(Instant.parse("2002-05-05T10:10:00.00Z"), true);
        createTaskItemEntity(Instant.parse("2222-05-05T10:10:00.00Z"), false);

        // when
        List<TaskItemEto> result = findTaskItemUc.findAllUncompletedOverDueTaskItems();

        // then
        assertThat(result).hasSize(1);
        TaskItemEto first = result.getFirst();
        assertThat(first.id()).isEqualTo(expected.getId());
    }

    private TaskItemEntity createTaskItemEntity(Instant deadline, boolean completed) {
        TaskItemEntity taskItemEntity = new TaskItemEntity();
        taskItemEntity.setName(DEFAULT_NAME);
        taskItemEntity.setDeadline(deadline);
        taskItemEntity.setCompleted(completed);
        return taskItemRepository.save(taskItemEntity);
    }
}