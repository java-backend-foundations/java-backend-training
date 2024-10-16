package com.capgemini.training.todo.task.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.Optional;

import com.capgemini.training.todo.TodoAppApplication;
import com.capgemini.training.todo.task.config.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.logic.FindPersonUc;
import com.capgemini.training.todo.task.logic.ManagePersonUc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonService.class)
public class PersonServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindPersonUc findPersonUc;

    @MockBean
    private ManagePersonUc managePersonUc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void findAllPersons_ShouldReturnPersonsList() throws Exception {
        PersonEto person = PersonEto.builder().id(1L).version(1).email("test@example.com").build();
        given(findPersonUc.findAllPersons()).willReturn(Arrays.asList(person));

        mockMvc.perform(get("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user")))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(person.id()))
                .andExpect(jsonPath("$[0].email").value(person.email()));
    }

    @Test
    public void findPerson_ShouldReturnPerson() throws Exception {
        Long personId = 1L;
        PersonEto person =
                PersonEto.builder().id(personId).version(1).email("test@example.com").build();
        given(findPersonUc.findPerson(personId)).willReturn(Optional.of(person));

        mockMvc.perform(get("/person/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(person.id()))
                .andExpect(jsonPath("$.email").value(person.email()));
    }

    @Test
    public void findPerson_NotFound() throws Exception {
        Long personId = 1L;
        given(findPersonUc.findPerson(personId)).willReturn(Optional.empty());

        mockMvc.perform(get("/person/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void savePerson_ShouldSaveAndReturnPerson() throws Exception {

        PersonEto newPerson =
                PersonEto.builder().id(null).version(0).email("test@example.com").build();
        String newPersonJson = objectMapper.writeValueAsString(newPerson);
        PersonEto savedPerson =
                PersonEto.builder().id(1L).version(1).email("test@example.com").build();
        given(managePersonUc.savePerson(any(PersonEto.class))).willReturn(savedPerson);

        mockMvc.perform(
                post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPersonJson)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(savedPerson.id()))
                .andExpect(jsonPath("$.email").value(savedPerson.email()));
    }


    @Test
    public void deletePerson_ShouldDeletePerson() throws Exception {
        Long personId = 1L;

        doNothing().when(managePersonUc).deletePerson(personId);

        mockMvc.perform(delete("/person/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("MAINTAINER"))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(managePersonUc, times(1)).deletePerson(personId);
    }

}


