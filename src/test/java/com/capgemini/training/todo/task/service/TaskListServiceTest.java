package com.capgemini.training.todo.task.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.capgemini.training.todo.task.common.TaskListCto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.logic.FindTaskListUc;
import com.capgemini.training.todo.task.logic.ManageTaskListUc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(TaskListService.class)
public class TaskListServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindTaskListUc findTaskListUc;

    @MockBean
    private ManageTaskListUc manageTaskListUc;

    @Test
    public void findAllTaskLists_ShouldReturnTaskList() throws Exception {
        TaskListEto taskListEto =
                TaskListEto.builder().id(1L).version(1).name("Task List 1").build();
        given(findTaskListUc.findAllTaskLists()).willReturn(Arrays.asList(taskListEto));

        mockMvc.perform(get("/task-list/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(taskListEto.id()))
                .andExpect(jsonPath("$[0].name").value(taskListEto.name()));
    }

    @Test
    public void findTaskList_ShouldReturnTaskListCto() throws Exception {
        TaskListEto taskListEto =
                TaskListEto.builder().id(1L).version(1).name("Task List 1").build();
        TaskListCto taskListCto = new TaskListCto(taskListEto, List.of());
        given(findTaskListUc.findTaskList(1L)).willReturn(Optional.of(taskListCto));

        mockMvc.perform(get("/task-list/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskListEto.id").value(taskListCto.taskListEto().id()))
                .andExpect(jsonPath("$.taskListEto.name").value(taskListCto.taskListEto().name()));
    }

    @Test
    public void findTaskList_NotFound() throws Exception {
        Long taskId = 1L;
        given(findTaskListUc.findTaskList(taskId)).willReturn(Optional.empty());

        mockMvc.perform(get("/task-list/{id}", taskId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveTaskList_ShouldSaveAndReturnTaskListCto() throws Exception {
        TaskListEto taskListEto = TaskListEto.builder().name("New Task List").build();
        TaskListCto newTaskListCto = new TaskListCto(taskListEto, List.of());
        TaskListCto savedTaskListCto = new TaskListCto(
                TaskListEto.builder().id(1L).version(1).name("New Task List").build(), List.of());

        given(manageTaskListUc.saveTaskList(any(TaskListCto.class))).willReturn(savedTaskListCto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String newTaskListCtoJson = objectMapper.writeValueAsString(newTaskListCto);

        mockMvc.perform(post("/task-list/").contentType(MediaType.APPLICATION_JSON)
                .content(newTaskListCtoJson)).andExpect(status().isOk())
                .andExpect(jsonPath("$.taskListEto.id").value(savedTaskListCto.taskListEto().id()))
                .andExpect(jsonPath("$.taskListEto.name")
                        .value(savedTaskListCto.taskListEto().name()));
    }

    @Test
    public void deleteTaskList_ShouldDeleteTaskList() throws Exception {
        Long taskListId = 1L;

        doNothing().when(manageTaskListUc).deleteTaskList(taskListId);

        mockMvc.perform(
                delete("/task-list/{id}", taskListId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(manageTaskListUc, times(1)).deleteTaskList(taskListId);
    }
}
