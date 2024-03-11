package com.capgemini.training.todo.task.logic.impl;

import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import com.capgemini.training.todo.task.logic.FindPersonUc;
import com.capgemini.training.todo.task.logic.mapper.PersonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindPersonUcImpl implements FindPersonUc {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public FindPersonUcImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public List<PersonEto> findAllPersons() {

        return personRepository.findAll().stream().map(personMapper::toPersonEto).toList();
    }

    @Override
    public Optional<PersonEto> findPerson(Long id) {

        return personRepository.findById(id).map(personMapper::toPersonEto);
    }
}
