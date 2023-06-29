package com.redhat.labs.springboot.flyway.application;

import java.util.List;

public interface TodoRepository {
  List<Todo> findAll();
  Todo save(Todo todo);
}
