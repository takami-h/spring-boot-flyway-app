package com.redhat.labs.springboot.flyway.adapter;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TodoEntryTest {
  Validator validator;

  @BeforeEach void setup() {
    var factory = new LocalValidatorFactoryBean();
    factory.afterPropertiesSet();

    validator = factory;
  }

  @Test void validationShouldWork() {
    var entry = new TodoEntry();

    entry.setTitle("");
    var violations = validator.validate(entry);
    assertViolation(violations, "title", "タイトルは必須です");

    entry.setTitle("verrrrrrrrrryyyyyyyyyyyyyyyyyyyyyyyyyyylooooooooooooooooooooooooooooooooooooooooooongtitleeeeeeeeeeeeeeeeee");
    violations = validator.validate(entry);
    assertViolation(violations, "title", "タイトルは100文字以内です");
  }

  <T> void assertViolation(Set<ConstraintViolation<T>> violations, String expectedPath, String expectedMessage) {
    var violation
      = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals(expectedPath))
          .findFirst();

    assertThat(violation.isPresent()).isTrue();
    assertThat(violation.get().getMessage()).isEqualTo(expectedMessage);
  }

  @Test void convertEntryToTodoWithoutDueto() {
    var entry = new TodoEntry();
    entry.setTitle("牛乳を買う");

    var todo = entry.toTodo();
    assertThat(todo.getTitle()).isEqualTo("牛乳を買う");
    assertThat(todo.getDueTo()).isNull();
    assertThat(todo.getCreatedAt()).isNotNull();
  }

  @Test void convertEntryToTodoWithDueto() {
    var entry = new TodoEntry();
    entry.setTitle("保険を見直す");
    entry.setDueTo("2023-12-31");

    var todo = entry.toTodo();
    assertThat(todo.getTitle()).isEqualTo("保険を見直す");
    assertThat(todo.getDueTo()).isEqualTo(LocalDate.of(2023, 12, 31));
    assertThat(todo.getCreatedAt()).isNotNull();
  }
}
