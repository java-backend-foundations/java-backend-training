package com.capgemini.training.todo.task.common;

import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record PersonCto (@Valid PersonEto personEto,
                         @Valid TaskListEto taskListEto) { }
