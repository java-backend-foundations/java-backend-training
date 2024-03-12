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
import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.logic.FindPersonUc;
import com.capgemini.training.todo.task.logic.ManagePersonUc;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("person")
@RequiredArgsConstructor
public class PersonService {

    final FindPersonUc findPersonUc;
    final ManagePersonUc managePersonUc;

    @GetMapping("/")
    List<PersonEto> findAllPersons() {
        return findPersonUc.findAllPersons();
    }

    @GetMapping("/{id}")
    PersonEto findPerson(@PathVariable("id") @NotNull Long id) {
        return findPersonUc.findPerson(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Person with id " + id + " does not exist."));
    }

    @PostMapping("/")
    PersonEto savePerson(@RequestBody @Valid PersonEto personEto) {
        return managePersonUc.savePerson(personEto);
    }

    @DeleteMapping("/{id}")
    void deletePerson(@PathVariable("id") @NotNull Long id) {
        managePersonUc.deletePerson(id);
    }

}
