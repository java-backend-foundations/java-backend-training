package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.PersonEto;

import java.util.List;
import java.util.Optional;

public interface FindPersonUc {

    List<PersonEto> findAllPersons();

    Optional<PersonEto> findPerson(Long id);
}
