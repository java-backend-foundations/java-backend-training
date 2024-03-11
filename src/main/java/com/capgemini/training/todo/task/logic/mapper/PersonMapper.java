package com.capgemini.training.todo.task.logic.mapper;

import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonEto toPersonEto(PersonEntity personEntity);

    PersonEntity toPersonEntity(PersonEto personEto);
}
