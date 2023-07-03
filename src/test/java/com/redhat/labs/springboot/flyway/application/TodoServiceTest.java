package com.redhat.labs.springboot.flyway.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
  @InjectMocks TodoService sut;
  @Mock TodoRepository repos;

  @Test void loadAll() {
    var todosSaved = List.of(
      Todo.newTodo("buy orange", Optional.empty()),
      Todo.newTodo("buy banana", Optional.empty()),
      Todo.newTodo("buy apple", Optional.empty())
    );
    given(repos.findAll())
      .willReturn(todosSaved);

    var todos = sut.loadAll();

    assertThat(todos)
      .hasSize(3)
      .containsAll(todosSaved);
  }

  @Test void save() {
    var newTodo = Todo.newTodo("buy milk", Optional.of(LocalDate.of(2023, 12, 31)));
    sut.save(newTodo);

    verify(repos).save(eq(newTodo));
  }
}
