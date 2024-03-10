package com.capgemini.training.todo.task.dataaccess.repository;

import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {

    //wszyszukiwać TaskListy po nazwie
    Optional<TaskListEntity> findFirstByName(String name);

    List<TaskListEntity> findAllByName(String name);


}
