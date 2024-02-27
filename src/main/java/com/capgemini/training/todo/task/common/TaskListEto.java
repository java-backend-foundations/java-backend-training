package com.capgemini.training.todo.task.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskListEto  {
    private Long id;
    private String name;
}
