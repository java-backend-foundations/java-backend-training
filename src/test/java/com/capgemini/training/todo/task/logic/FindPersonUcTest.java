package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FindPersonUcTest {

    private static final String DEFAULT_EMAIL = "some@email.com";

    @Autowired
    private FindPersonUc findPersonUc;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        personRepository.deleteAll();
    }

    @Test
    void findAllPersons() {
        // given
        PersonEntity personEntity = new PersonEntity();
        personEntity.setEmail(DEFAULT_EMAIL);
        personRepository.save(personEntity);

        // when
        List<PersonEto> result = findPersonUc.findAllPersons();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().email()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void findPerson() {
        // given
        PersonEntity personEntity = new PersonEntity();
        personEntity.setEmail(DEFAULT_EMAIL);
        personEntity = personRepository.save(personEntity);

        // when
        Optional<PersonEto> result = findPersonUc.findPerson(personEntity.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo(DEFAULT_EMAIL);
    }
}