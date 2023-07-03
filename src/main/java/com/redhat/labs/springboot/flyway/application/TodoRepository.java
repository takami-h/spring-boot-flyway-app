package com.redhat.labs.springboot.flyway.application;

import java.util.List;

public interface TodoRepository {
  List<Todo> findAll();
  List<Todo> findUnfinished();
  Todo save(Todo todo);
}
