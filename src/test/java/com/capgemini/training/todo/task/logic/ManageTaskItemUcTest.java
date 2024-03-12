package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.repository.TaskItemRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ManageTaskItemUcTest {

    private static final String DEFAULT_NAME = "Default";

    private static final String UPDATED_NAME = "Updated";

    @Autowired
    private ManageTaskItemUc manageTaskItemUc;

    @Autowired
    private TaskItemRepository taskItemRepository;

    @Test
    public void saveTaskItem_shouldCreateTaskItem() {
        // given
        TaskItemEto taskItemEto = TaskItemEto.builder().name(DEFAULT_NAME).deadline(Instant.now()).build();

        // when
        TaskItemEto result = manageTaskItemUc.saveTaskItem(taskItemEto);

        // then
        Optional<TaskItemEntity> taskItemEntity = taskItemRepository.findById(result.id());
        assertThat(taskItemEntity).isPresent();
        assertThat(taskItemEntity.get().getName()).isEqualTo(DEFAULT_NAME);
        assertThat(taskItemEntity.get().isCompleted()).isFalse();
    }

    @Test
    public void saveTaskItem_shouldNotCreateTaskItemWithTooShortName() {
        // given
        TaskItemEto taskItemEto = TaskItemEto.builder().name("x").deadline(Instant.now()).build();

        // when
        assertThatThrownBy(() -> manageTaskItemUc.saveTaskItem(taskItemEto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("saveTaskItem.taskItemEto.name");
    }

    @Test
    public void saveTaskItem_shouldNotCreateTaskItemWithTooLongName() {
        // given
        TaskItemEto taskItemEto = TaskItemEto.builder().name("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx").build();

        // when
        assertThatThrownBy(() -> manageTaskItemUc.saveTaskItem(taskItemEto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("saveTaskItem.taskItemEto.name");
    }

    @Test
    public void saveTaskItem_shouldUpdateTaskItem() {
        // given
        TaskItemEto taskItemEto = TaskItemEto.builder().name(DEFAULT_NAME).deadline(Instant.now()).build();
        taskItemEto = manageTaskItemUc.saveTaskItem(taskItemEto);
        taskItemEto = TaskItemEto.builder().id(taskItemEto.id()).version(taskItemEto.version()).name(UPDATED_NAME).completed(true).build();

        // when
        TaskItemEto result = manageTaskItemUc.saveTaskItem(taskItemEto);

        // then
        Optional<TaskItemEntity> taskItemEntity = taskItemRepository.findById(result.id());
        assertThat(taskItemEntity).isPresent();
        assertThat(taskItemEntity.get().getName()).isEqualTo(UPDATED_NAME);
        assertThat(taskItemEntity.get().isCompleted()).isTrue();
    }

    @Test
    public void deleteTaskItem() {
        // given
        TaskItemEto taskItemEto = TaskItemEto.builder().name(DEFAULT_NAME).deadline(Instant.now()).build();
        taskItemEto = manageTaskItemUc.saveTaskItem(taskItemEto);
        assertThat(taskItemRepository.existsById(taskItemEto.id())).isTrue();

        // when
        manageTaskItemUc.deleteTaskItem(taskItemEto.id());

        // then
        assertThat(taskItemRepository.existsById(taskItemEto.id())).isFalse();
    }

    @Test
    public void deleteTaskItem_shouldValidateIfIdIsNull() {
        // when
        assertThatThrownBy(() -> manageTaskItemUc.deleteTaskItem(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("deleteTaskItem.id");
    }
}