package com.redhat.labs.springboot.flyway.adapter;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.redhat.labs.springboot.flyway.application.Todo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Tag("IT")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {
  @LocalServerPort int port;

  @Autowired TodoJpaRepository todoRepos;

  @BeforeEach
  @Transactional
  void setup() {
    RestAssured.port = this.port;

    todoRepos.deleteAll();
    ((JpaRepository<Todo, Long>) todoRepos).save(Todo.newTodo("牛乳を買う", Optional.of(LocalDate.of(2023, 12, 1))));
    ((JpaRepository<Todo, Long>) todoRepos).save(Todo.newTodo("家賃を払う", Optional.of(LocalDate.of(2023, 12, 31))));
  }

  @Test void getTodo() {
    when()
      .get("/todos")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("title", hasItems("牛乳を買う", "家賃を払う"));
  }

  @Test void postTodo() {
    var lastId = todoRepos.findAll().stream()
      .map(todo -> todo.getId())
      .max(Comparator.naturalOrder())
        .get();

    var todo = new TodoEntry();
    todo.setTitle("保険を見直す");
    todo.setDueTo("2023-12-31");

    given()
      .contentType(ContentType.JSON)
      .body(todo)
    .when()
      .post("/todos")
    .then()
      .statusCode(HttpStatus.CREATED.value())
      .header("Location", "/todos/" + (lastId + 1));
    
    when()
      .get("/todos")
    .then()
      .body("title[2]", equalTo("保険を見直す"));
  }

  @Test void postTodo400IfInvalidDateFormat() {
    var todo = new TodoEntry();
    todo.setTitle("保険を見直す");
    todo.setDueTo("2023/12/31"); // BAD DATE FORMAT!

    given()
      .contentType(ContentType.JSON)
      .body(todo)
    .when()
      .post("/todos")
    .then()
      .statusCode(HttpStatus.BAD_REQUEST.value());
  }
}
