package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.PersonCto;
import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import com.capgemini.training.todo.task.logic.mapper.PersonMapper;
import com.capgemini.training.todo.task.logic.mapper.PersonMapperImpl;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagePersonUcImplTest {

    private static final String DEFAULT_EMAIL = "some@email.com";

    private static final String DEFAULT_TASK_LIST_NAME = "Default Name";

    @InjectMocks
    private ManagePersonUcImpl managePersonUc;

    @Mock
    private PersonRepository personRepository;
    @Spy
    private PersonMapper personMapper = new PersonMapperImpl();
    @Mock
    private TaskListRepository taskListRepository;
    @Spy
    private TaskListMapper taskListMapper = new TaskListMapperImpl();

    @Test
    void savePersonEto() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();
        when(personRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        PersonEto result = managePersonUc.savePerson(personEto);

        // then
        ArgumentCaptor<PersonEntity> personEntityCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        verify(personRepository).saveAndFlush(personEntityCaptor.capture());
        assertThat(personEntityCaptor.getValue().getEmail()).isEqualTo(DEFAULT_EMAIL);

        assertThat(result.email()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void SavePersonCto() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        PersonCto personCto = PersonCto.builder().personEto(personEto).taskListEto(taskListEto).build();

        when(personRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);
        when(taskListRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);

        // then
        PersonCto result = managePersonUc.savePerson(personCto);

        // then
        assertThat(result.personEto()).isNotNull();
        assertThat(result.personEto().email()).isEqualTo(DEFAULT_EMAIL);
        assertThat(result.taskListEto()).isNotNull();
        assertThat(result.taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);

        verify(personRepository).saveAndFlush(any());
        verify(taskListRepository).saveAndFlush(any());
    }

    @Test
    void deletePerson() {
        // given
        // when
        managePersonUc.deletePerson(1L);

        // then
        verify(personRepository).deleteById(1L);
    }
}