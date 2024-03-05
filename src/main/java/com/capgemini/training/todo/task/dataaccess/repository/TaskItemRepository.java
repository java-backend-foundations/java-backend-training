package com.capgemini.training.todo.task.dataaccess.repository;


import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItemEntity, Long> {

    @Query("SELECT item FROM TaskItemEntity item WHERE item.deadline < :deadline")
    List<TaskItemEntity> findByDeadlineBefore(@Param("deadline") Instant deadline);
}
