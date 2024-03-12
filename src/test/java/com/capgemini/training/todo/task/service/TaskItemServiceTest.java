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
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.capgemini.training.todo.task.common.TaskItemEto;
import com.capgemini.training.todo.task.logic.FindTaskItemUc;
import com.capgemini.training.todo.task.logic.ManageTaskItemUc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



@WebMvcTest(TaskItemService.class)
public class TaskItemServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindTaskItemUc findTaskItemUc;

    @MockBean
    private ManageTaskItemUc manageTaskItemUc;

    @Test
    public void findAllTaskItems_ShouldReturnTaskItemList() throws Exception {
        TaskItemEto taskItem = TaskItemEto.builder().id(1L).version(1).name("Task 1")
                .completed(false).deadline(Instant.now()).build();
        given(findTaskItemUc.findAllTaskItems()).willReturn(Arrays.asList(taskItem));

        mockMvc.perform(get("/task-item/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(taskItem.id()))
                .andExpect(jsonPath("$[0].name").value(taskItem.name()));
    }

    @Test
    public void findTaskItem_ShouldReturnTaskItem() throws Exception {
        Long taskId = 1L;
        TaskItemEto taskItem = TaskItemEto.builder().id(taskId).version(1).name("Task 1")
                .completed(false).deadline(Instant.now()).build();
        given(findTaskItemUc.findTaskItem(taskId)).willReturn(Optional.of(taskItem));

        mockMvc.perform(get("/task-item/{id}", taskId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(taskItem.id()))
                .andExpect(jsonPath("$.name").value(taskItem.name()));
    }

    @Test
    public void findTaskItem_NotFound() throws Exception {
        Long taskId = 1L;
        given(findTaskItemUc.findTaskItem(taskId)).willReturn(Optional.empty());

        mockMvc.perform(get("/task-item/{id}", taskId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveTaskItem_ShouldSaveAndReturnTaskItem() throws Exception {
        TaskItemEto newTaskItem = TaskItemEto.builder().name("New Task").completed(false)
                .deadline(Instant.now()).build();
        TaskItemEto savedTaskItem = TaskItemEto.builder().id(1L).version(1).name("New Task")
                .completed(false).deadline(Instant.now()).build();
        given(manageTaskItemUc.saveTaskItem(any(TaskItemEto.class))).willReturn(savedTaskItem);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String newTaskItemJson = objectMapper.writeValueAsString(newTaskItem);

        mockMvc.perform(post("/task-item/").contentType(MediaType.APPLICATION_JSON)
                .content(newTaskItemJson)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTaskItem.id()))
                .andExpect(jsonPath("$.name").value(savedTaskItem.name()));
    }

    @Test
    public void deleteTaskList_ShouldDeleteTaskItem() throws Exception {
        Long taskId = 1L;

        doNothing().when(manageTaskItemUc).deleteTaskItem(taskId);

        mockMvc.perform(delete("/task-item/{id}", taskId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(manageTaskItemUc, times(1)).deleteTaskItem(taskId);
    }

}
