package com.capgemini.training.todo.task.dataaccess.repository;

import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TaskListRepositoryTest {

    @Autowired
    private TaskListRepository taskListRepository;

    @Test
    void testFindFirstByName() {

        final String testName = "test";
        final TaskListEntity providedEntity = new TaskListEntity();
        providedEntity.setName(testName);
        taskListRepository.save(providedEntity);

        Optional<TaskListEntity> firstByName = taskListRepository.findFirstByName(testName);

        assertTrue(firstByName.isPresent());
        firstByName.ifPresent(taskList -> {
            assertEquals(testName, taskList.getName());
        });
    }
}
