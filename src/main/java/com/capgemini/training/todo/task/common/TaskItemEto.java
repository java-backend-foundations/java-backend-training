package com.capgemini.training.todo.task.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TaskItemEto(Long id,
                          @NotNull @Size(min = 2, max = 40) String name,
                          boolean completed,
                          Instant deadline) { }
