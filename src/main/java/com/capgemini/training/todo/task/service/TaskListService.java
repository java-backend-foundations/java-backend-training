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
import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.logic.FindTaskListUc;
import com.capgemini.training.todo.task.logic.ManageTaskListUc;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("task-list")
@RequiredArgsConstructor
public class TaskListService {

    final FindTaskListUc findTaskListUc;
    final ManageTaskListUc manageTaskListUc;

    @GetMapping("/")
    List<TaskListEto> findAllTaskItems() {
        return findTaskListUc.findAllTaskLists();
    }

    @GetMapping("/{id}")
    TaskListCto findTaskList(@PathVariable("id") @NotNull Long id) {
        return findTaskListUc.findTaskList(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "TaskList with id " + id + " does not exist."));
    }

    @PostMapping("/")
    TaskListCto saveTaskList(@RequestBody @Valid TaskListCto taskListCto) {
        return manageTaskListUc.saveTaskList(taskListCto);
    }

    @DeleteMapping("/{id}")
    void deleteTaskList(@PathVariable("id") @NotNull Long id) {
        manageTaskListUc.deleteTaskList(id);
    }
}
