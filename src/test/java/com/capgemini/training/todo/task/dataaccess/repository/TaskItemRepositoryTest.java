package com.capgemini.training.todo.task.dataaccess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.capgemini.training.todo.task.dataaccess.repository.criteria.TaskItemCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;

@DataJpaTest
class TaskItemRepositoryTest {

	@Autowired
	private TaskItemRepository taskItemRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@BeforeEach
	void setup() {
		taskItemRepository.deleteAll();
	}

	@Test
	void testByCriteria(){
		// given
		// when
		TaskItemCriteria criteria = new TaskItemCriteria("Update Instruction on Confluence", "Business Analysis Tasks");
		taskItemRepository.findByCriteria(criteria, entityManager);
		// then
	}

	
	@Test
	void testFindByDeadlineBefore() {
		TaskItemEntity task1 = new TaskItemEntity();
		task1.setDeadline(Instant.parse("2002-05-05T10:10:00.00Z"));
		task1.setName("task1");
		task1.setCompleted(true);
		TaskItemEntity task2 = new TaskItemEntity();
		task2.setDeadline(Instant.parse("2050-05-05T23:25:00.00Z"));
		task2.setName("task2");
		taskItemRepository.saveAll(Arrays.asList(task1, task2));
		
		List<TaskItemEntity> result = taskItemRepository.findByDeadlineBefore(Instant.now());
		
		assertThat(result).isNotEmpty().hasSize(1);
		TaskItemEntity task = result.get(0);
		assertThat(task.getName()).isEqualTo("task1");
		assertThat(task.isCompleted()).isTrue();
		}

	@Test
	void testFindByCompletedAndDeadlineBefore() {
		TaskItemEntity task1 = new TaskItemEntity();
		task1.setDeadline(Instant.parse("2002-05-05T10:10:00.00Z"));
		task1.setName("task1");
		task1.setCompleted(true);
		TaskItemEntity task2 = new TaskItemEntity();
		task2.setDeadline(Instant.parse("2050-05-05T23:25:00.00Z"));
		task2.setName("task2");
		taskItemRepository.saveAll(Arrays.asList(task1, task2));

		List<TaskItemEntity> result = taskItemRepository.findByCompletedAndDeadlineBefore(true, Instant.now());

		assertThat(result).isNotEmpty().hasSize(1);
		TaskItemEntity task = result.get(0);
		assertThat(task.getName()).isEqualTo("task1");
		assertThat(task.isCompleted()).isTrue();
	}
	
}
