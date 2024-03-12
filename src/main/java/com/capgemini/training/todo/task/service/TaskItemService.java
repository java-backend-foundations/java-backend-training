package com.capgemini.training.todo.task.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.logic.FindTaskItemUc;
import com.capgemini.training.todo.task.logic.ManageTaskItemUc;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("task-item")
@RequiredArgsConstructor
public class TaskItemService {

    final FindTaskItemUc findTaskItemUc;
    final ManageTaskItemUc manageTaskItemUc;

    @GetMapping("/")
    List<TaskItemEto> findAllTaskItems() {
        return findTaskItemUc.findAllTaskItems();
    }

    @GetMapping("/{id}")
    TaskItemEto findTaskItem(@PathVariable("id") @NotNull Long id) {
        return findTaskItemUc.findTaskItem(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "TaskItem with id " + id + " does not exist."));
    }

    @PostMapping("/")
    TaskItemEto saveTaskItem(@RequestBody @Valid TaskItemEto taskListEto) {
        return manageTaskItemUc.saveTaskItem(taskListEto);
    }

    @DeleteMapping("/{id}")
    void deleteTaskList(@PathVariable("id") @NotNull Long id) {
        manageTaskItemUc.deleteTaskItem(id);
    }

}
