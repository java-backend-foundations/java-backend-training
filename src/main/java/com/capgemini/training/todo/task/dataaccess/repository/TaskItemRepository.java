package com.capgemini.training.todo.task.dataaccess.repository;


import com.capgemini.training.todo.task.dataaccess.entity.TaskItemEntity;
import com.capgemini.training.todo.task.dataaccess.entity.TaskListEntity;
import com.capgemini.training.todo.task.dataaccess.repository.criteria.TaskItemCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.jpa.EntityManagerProxy;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItemEntity, Long> {

    //i taski, po completed i po dacie (by można było sobie znaleźć np. nieskonczone z przekrczonym terminem)
    @Query("SELECT item FROM TaskItemEntity item WHERE item.deadline < :deadline")
    List<TaskItemEntity> findByDeadlineBefore(@Param("deadline") Instant deadline);

    @Query("SELECT item FROM TaskItemEntity item WHERE item.completed = :completed AND item.deadline < :deadline")
    List<TaskItemEntity> findByCompletedAndDeadlineBefore(@Param("completed") boolean completed, @Param("deadline") Instant deadline);

    default List<TaskItemEntity> findByCriteria(TaskItemCriteria searchCriteria, EntityManager entityManager) {
        Objects.requireNonNull(searchCriteria, "Invalid input: searchCriteria - cannot be null!");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder(); // initialize criteria builder, to build queries
        CriteriaQuery<TaskItemEntity> criteriaQuery = builder.createQuery(TaskItemEntity.class); // initialize Query structure
        Root<TaskItemEntity> root = criteriaQuery.from(TaskItemEntity.class); //Initialise "select from"
        List<Predicate> predicateList = new ArrayList<>();

        if (searchCriteria.taskItemName() != null) {
            predicateList.add(builder.like(root.get("name"), searchCriteria.taskItemName()));
        }

        if (searchCriteria.taskListName() != null) {
            Join<TaskItemEntity, TaskListEntity> joinedList = root.join("taskList", JoinType.INNER);
            predicateList.add(builder.like(joinedList.get("name"), searchCriteria.taskListName()));
        }

        Predicate[] predicateArray = predicateList.toArray(new Predicate[0]);
        criteriaQuery.where(predicateArray); //API made for lists does not like lists
        TypedQuery<TaskItemEntity> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

}
