package com.capgemini.training.todo.task.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PersonEto(Long id,
                        @NotNull @Email String email) { }
