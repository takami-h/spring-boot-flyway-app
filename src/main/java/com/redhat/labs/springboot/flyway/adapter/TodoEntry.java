package com.redhat.labs.springboot.flyway.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.redhat.labs.springboot.flyway.application.Todo;

public class TodoEntry {
  private static final DateTimeFormatter DATEF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @NotEmpty(message = "{TodoEntry.title.NotEmpty}")
  @Size(max = 100, message = "{TodoEntry.title.Size}")
  private String title;

  private String dueTo;

  public Todo toTodo() {
    var dueToDate = Optional.ofNullable(dueTo).map(str -> LocalDate.from(DATEF.parse(str)));

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
