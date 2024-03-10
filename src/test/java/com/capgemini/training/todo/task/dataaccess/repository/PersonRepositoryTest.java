package com.capgemini.training.todo.task.dataaccess.repository;

import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testFindByQueryDSL(){
        // given when
        List<PersonEntity> result = personRepository.findCustomByEmailWithTaskListName(
                "ba@capgemini.com", "Business Analysis Tasks", entityManager);
        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

}