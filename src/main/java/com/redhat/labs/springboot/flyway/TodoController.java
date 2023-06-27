package com.redhat.labs.springboot.flyway;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class TodoController {
  private static final DateTimeFormatter DATEF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private TodoRepository repos;

  public TodoController(TodoRepository repos) {
    this.repos = repos;
  }

  @GetMapping(value = "/todos", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<List<Todo>> loadAllTodos() {
    var todos = repos.findAll();

    return ResponseEntity.ok(todos);
  }

  @PostMapping("/todos")
  public ResponseEntity<Void> addTodo(
    @RequestParam("title") String title,
    @RequestParam("dueTo") Optional<String> dueTo) {

    Optional<LocalDate> dueToDate = dueTo.map(due -> LocalDate.from(DATEF.parse(due)));

    repos.save(Todo.newTodo(title, dueToDate));

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
