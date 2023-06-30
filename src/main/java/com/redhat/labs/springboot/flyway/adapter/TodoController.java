package com.redhat.labs.springboot.flyway.adapter;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import com.redhat.labs.springboot.flyway.application.Todo;
import com.redhat.labs.springboot.flyway.application.TodoService;

@RestController
@Transactional
public class TodoController {
  private static final DateTimeFormatter DATEF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private TodoService service;

  public TodoController(TodoService service) {
    this.service = service;
  }

  @GetMapping(value = "/todos", produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<List<Todo>> loadAllTodos() {
    var todos = service.loadAll();

    return ResponseEntity.ok(todos);
  }

  @PostMapping("/todos")
  public ResponseEntity<Void> addTodo(
    @RequestParam("title") String title,
    @RequestParam("dueTo") Optional<String> dueTo) {

    Optional<LocalDate> dueToDate = dueTo.map(due -> LocalDate.from(DATEF.parse(due)));

    var saved = service.save(Todo.newTodo(title, dueToDate));

    var location = new UriTemplate("/todos/{id}").expand(saved.getId());
    return ResponseEntity.created(location).build();
  }
}
