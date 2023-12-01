package com.redhat.labs.springboot.flyway.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private LocalDate dueTo;
  private Boolean done;
  private LocalDateTime createdAt;

  public static Todo newTodo(String title, Optional<LocalDate> dueTo) {
    var todo = new Todo();
    todo.setCreatedAt(LocalDateTime.now());
    todo.setDone(Boolean.FALSE);

    todo.setTitle(title);
    dueTo.ifPresent(date -> todo.setDueTo(date));

    return todo;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public LocalDate getDueTo() {
    return dueTo;
  }
  public void setDueTo(LocalDate dueTo) {
    this.dueTo = dueTo;
  }
  public Boolean isDone() {
    return done;
  }
  public void setDone(Boolean done) {
    this.done = done;
  }
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Todo other = (Todo) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
