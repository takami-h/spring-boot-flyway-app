package com.redhat.labs.springboot.flyway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional @Service
public class TodoService {
  private TodoRepository repos;
  public TodoService(TodoRepository repos) {
    this.repos = repos;
  }

  public List<Todo> loadAll() {
    return repos.findAll();
  }
  public Todo save(Todo todo) {
    var saved = repos.save(todo);
    return saved;
  }
}
