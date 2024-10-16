package com.capgemini.training.todo.task.logic;

import com.capgemini.training.todo.task.common.PersonCto;
import com.capgemini.training.todo.task.common.PersonEto;
import com.capgemini.training.todo.task.common.TaskListEto;
import com.capgemini.training.todo.task.config.security.AccessControl;
import com.capgemini.training.todo.task.dataaccess.entity.PersonEntity;
import com.capgemini.training.todo.task.dataaccess.repository.PersonRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@WithMockUser(username = "test", roles = AccessControl.ROLE_ADMIN)
class ManagePersonUcTest {

    private static final String DEFAULT_EMAIL = "some@email.com";
    private static final String DEFAULT_TASK_LIST_NAME = "Default Name";
    private static final String UPDATED_EMAIL = "new@email.com";

    @Autowired
    private ManagePersonUc managePersonUc;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void savePerson_shouldCreatePerson() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();

        // when
        PersonEto result = managePersonUc.savePerson(personEto);

        // then
        Optional<PersonEntity> personEntity = personRepository.findById(result.id());
        assertThat(personEntity).isPresent();
        assertThat(personEntity.get().getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void savePerson_shouldNotCreatePersonEtoWithInvalidEmail() {
        // given
        PersonEto personEto = PersonEto.builder().email("email").build();

        // when
        assertThatThrownBy(() -> managePersonUc.savePerson(personEto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("savePerson.personEto.email");
    }

    @Test
    public void savePerson_shouldUpdatePerson() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();
        personEto = managePersonUc.savePerson(personEto);
        personEto = PersonEto.builder().id(personEto.id()).version(personEto.version()).email(UPDATED_EMAIL).build();

        // when
        PersonEto result = managePersonUc.savePerson(personEto);

        // then
        Optional<PersonEntity> personEntity = personRepository.findById(result.id());
        assertThat(personEntity).isPresent();
        assertThat(personEntity.get().getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    public void savePerson_shouldCreatePersonWithTaskList() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        PersonCto personCto = PersonCto.builder().personEto(personEto).taskListEto(taskListEto).build();

        // when
        PersonCto result = managePersonUc.savePerson(personCto);

        // then
        assertThat(result.personEto()).isNotNull();
        assertThat(result.personEto().email()).isEqualTo(DEFAULT_EMAIL);
        assertThat(result.taskListEto()).isNotNull();
        assertThat(result.taskListEto().name()).isEqualTo(DEFAULT_TASK_LIST_NAME);

        Optional<PersonEntity> personEntity = personRepository.findById(result.personEto().id());
        assertThat(personEntity).isPresent();
        assertThat(personEntity.get().getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(personEntity.get().getTaskList()).isNotNull();
        assertThat(personEntity.get().getTaskList().getName()).isEqualTo(DEFAULT_TASK_LIST_NAME);
    }

    @Test
    public void savePerson_shouldNotCreatePersonCtoWithInvalidEmail() {
        // given
        PersonEto personEto = PersonEto.builder().email("email").build();
        TaskListEto taskListEto = TaskListEto.builder().name(DEFAULT_TASK_LIST_NAME).build();
        PersonCto personCto = PersonCto.builder().personEto(personEto).taskListEto(taskListEto).build();

        // when
        assertThatThrownBy(() -> managePersonUc.savePerson(personCto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("savePerson.personCto.personEto.email");
    }

    @Test
    public void savePerson_shouldNotCreatePersonCtoWithInvalidItem() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();
        TaskListEto taskListEto = TaskListEto.builder().name("").build();
        PersonCto personCto = PersonCto.builder().personEto(personEto).taskListEto(taskListEto).build();

        // when
        assertThatThrownBy(() -> managePersonUc.savePerson(personCto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("savePerson.personCto.taskListEto.name");
    }

    @Test
    public void deletePerson() {
        // given
        PersonEto personEto = PersonEto.builder().email(DEFAULT_EMAIL).build();
        personEto = managePersonUc.savePerson(personEto);
        assertThat(personRepository.existsById(personEto.id())).isTrue();

        // when
        managePersonUc.deletePerson(personEto.id());

        // then
        assertThat(personRepository.existsById(personEto.id())).isFalse();
    }

    @Test
    public void deletePerson_shouldValidateIfIdIsNull() {
        // when
        assertThatThrownBy(() -> managePersonUc.deletePerson(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("deletePerson.id");
    }
}