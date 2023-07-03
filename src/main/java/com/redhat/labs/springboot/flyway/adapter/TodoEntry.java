package com.redhat.labs.springboot.flyway.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.redhat.labs.springboot.flyway.application.Todo;

public class TodoEntry {
  private static final DateTimeFormatter DATEF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private String title;
  private String dueTo;

  public Todo toTodo() {
    Optional<LocalDate> dueToDate = dueTo != null ? Optional.of(LocalDate.from(DATEF.parse(dueTo))) : Optional.empty();

    return Todo.newTodo(title, dueToDate);
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDueTo() {
    return dueTo;
  }
  public void setDueTo(String dueTo) {
    this.dueTo = dueTo;
  }
}
