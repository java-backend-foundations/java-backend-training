package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.PersonCto;
import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import com.capgemini.training.todo.task.dataaccess.repository.TaskListRepository;
import com.capgemini.training.todo.task.logic.ManagePersonUc;
import com.capgemini.training.todo.task.logic.mapper.PersonMapper;
import com.capgemini.training.todo.task.logic.mapper.TaskListMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
public class ManagePersonUcImpl implements ManagePersonUc {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final TaskListRepository taskListRepository;
    private final TaskListMapper taskListMapper;

    public ManagePersonUcImpl(PersonRepository personRepository, PersonMapper personMapper, TaskListRepository taskListRepository, TaskListMapper taskListMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.taskListRepository = taskListRepository;
        this.taskListMapper = taskListMapper;
    }

    @Override
    public PersonEto savePerson(@Valid PersonEto personEto) {

        PersonEntity personEntity = personMapper.toPersonEntity(personEto);
        personEntity = personRepository.saveAndFlush(personEntity);
        return personMapper.toPersonEto(personEntity);
    }

    @Override
    public PersonCto savePerson(@Valid PersonCto personCto) {

        PersonEntity personEntity = personMapper.toPersonEntity(personCto.personEto());
        TaskListEntity taskListEntity = taskListMapper.toTaskListEntity(personCto.taskListEto());

        // No cascade type was defined for the relation, so we have to handle it manually
        taskListEntity = taskListRepository.saveAndFlush(taskListEntity);
        personEntity.setTaskList(taskListEntity);

        personEntity = personRepository.saveAndFlush(personEntity);
        return PersonCto.builder()
                .personEto(personMapper.toPersonEto(personEntity))
                .taskListEto(taskListMapper.toTaskListEto(personEntity.getTaskList()))
                .build();
    }

    @Override
    public void deletePerson(@NotNull Long id) {

        personRepository.deleteById(id);
    }
}
