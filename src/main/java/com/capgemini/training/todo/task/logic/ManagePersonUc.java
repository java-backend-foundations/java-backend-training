package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.PersonCto;
import com.capgemini.training.todo.task.common.PersonEto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface ManagePersonUc {
    PersonEto savePerson(@Valid PersonEto personEto);

    PersonCto savePerson(@Valid PersonCto personCto);

    void deletePerson(@NotNull Long id);
}
