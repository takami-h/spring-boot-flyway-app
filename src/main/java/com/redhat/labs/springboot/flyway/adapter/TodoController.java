package com.redhat.labs.springboot.flyway.adapter;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import com.redhat.labs.springboot.flyway.application.Todo;
import com.redhat.labs.springboot.flyway.application.TodoService;

@RestController
@Transactional
public class TodoController {
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
  public ResponseEntity<Void> addTodo(@RequestBody TodoEntry todoEntry) {
    var saved = service.save(todoEntry.toTodo());

    var location = new UriTemplate("/todos/{id}").expand(saved.getId());
    return ResponseEntity.created(location).build();
  }
}
