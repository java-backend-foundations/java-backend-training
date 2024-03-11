package com.capgemini.training.todo.task.common;

import jakarta.validation.Valid;
import lombok.Builder;

import java.util.List;

@Builder
public record TaskListCto(@Valid TaskListEto taskListEto,
                          List<@Valid TaskItemEto> taskItemEtos) { }
