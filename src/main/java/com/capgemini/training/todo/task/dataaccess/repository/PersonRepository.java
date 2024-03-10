package com.capgemini.training.todo.task.dataaccess.repository;

import com.capgemini.training.todo.task.dataaccess.entity.QPersonEntity;
import com.capgemini.training.todo.task.dataaccess.entity.QTaskListEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    default List<PersonEntity> findCustomByEmailWithTaskListName(String email, String taskListName, EntityManager entityManager) {

        QPersonEntity person = QPersonEntity.personEntity;
        QTaskListEntity taskList = QPersonEntity.personEntity.taskList;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        return jpaQueryFactory.select(person).from(person)
                .innerJoin(taskList)
                .where(person.email.eq(email)
                        .and(taskList.name.like(taskListName))) //showcase of multiple clauses
                .orderBy(person.email.desc(), taskList.name.asc())
                .fetch();
    }
}