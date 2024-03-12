package com.capgemini.training.todo.task.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TaskListEto  (Long id,
                            int version,
                            @NotNull @Size(min = 5) String name) { }
