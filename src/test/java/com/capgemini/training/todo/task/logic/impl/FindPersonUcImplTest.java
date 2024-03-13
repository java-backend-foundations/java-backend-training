package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import com.capgemini.training.todo.task.logic.mapper.PersonMapper;
import com.capgemini.training.todo.task.logic.mapper.PersonMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPersonUcImplTest {

    private static final String DEFAULT_EMAIL = "some@email.com";

    @InjectMocks
    private FindPersonUcImpl findPersonUc;

    @Mock
    private PersonRepository personRepository;

    @Spy
    private PersonMapper personMapper = new PersonMapperImpl();

    @Test
    void findAllPersons() {
        // given
        PersonEntity personEntity = new PersonEntity();
        personEntity.setEmail(DEFAULT_EMAIL);

        when(personRepository.findAll()).thenReturn(List.of(personEntity, personEntity, personEntity));

        // when
        List<PersonEto> result = findPersonUc.findAllPersons();

        // then
        assertThat(result).hasSize(3);
        assertThat(result).allSatisfy(personEto -> assertThat(personEto.email()).isEqualTo(DEFAULT_EMAIL));
    }

    @Test
    void findPerson() {
        // given
        PersonEntity personEntity = new PersonEntity();
        personEntity.setEmail(DEFAULT_EMAIL);

        when(personRepository.findById(1L)).thenReturn(Optional.of(personEntity));

        // when
        Optional<PersonEto> result = findPersonUc.findPerson(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo(DEFAULT_EMAIL);
    }
}