package com.capgemini.training.todo.task.dataaccess.repository;

import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest this has web and allows for easier configs, but is slower
@DataJpaTest
public class TaskListRepositoryTest {

    @Autowired
    private TaskListRepository taskListRepository; //some IDE's wrongfully mark this var as unused

    @Test
    void testFindAll() {
        //given when
        List<TaskListEntity> result = taskListRepository.findAll();

        //then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        //Notice, that the number of objects are equal to entities inserted by mockdata script launched by flyway.
        //We could also define separate test/resources migration scripts, but we won't do that (let's spare the time, you can google it if you want)
    }

    @Test
    void testFindAllByName() {
        //given
        String name = "Developer Tasks";
        // when
        List<TaskListEntity> result = taskListRepository.findAllByName(name);
        //then
        assertThat(result).hasSize(1);
    }

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
