package com.capgemini.training.todo.task.dataaccess.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TASK_ITEM")
@Getter
@Setter
public class TaskItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private int version;

    private String name;

    private boolean completed;

    private Instant deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    private TaskListEntity taskList;
}
