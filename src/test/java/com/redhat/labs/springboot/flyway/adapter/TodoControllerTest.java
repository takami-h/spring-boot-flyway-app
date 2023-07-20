package com.redhat.labs.springboot.flyway.adapter;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

import org.json.JSONArray;
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
import io.restassured.path.json.JsonPath;

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

  @Test void getTodo() throws Exception {
    var response = when()
      .get("/todos")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("title", hasItems("牛乳を買う", "家賃を払う")); // assertion using fluent api

    // or assertion using json
    var json = response.extract().asString();
    var todosFetched = new JSONArray(json);
    assertThat(todosFetched.getJSONObject(0).getString("title")).isEqualTo("牛乳を買う");

    // or JsonPath
    var todosFetched2 = JsonPath.from(json);
    assertThat(todosFetched2.getString("[0].title")).isEqualTo("牛乳を買う");

    // or deserialized object
    var todosFetched3 = response.extract().as(Todo[].class);
    assertThat(todosFetched3[0].getTitle()).isEqualTo("牛乳を買う");
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

  @Test void postTodo400IfTitleEmpty() {
    var todo = new TodoEntry();
    todo.setTitle("tooooooooooooooooooolooooooooooooooooooooongtitttttttttttttttttttttttttttttttttttttttttttttttttttttle");
    todo.setDueTo("");

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .body(todo)
    .when()
      .post("/todos")
    .then()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body(
        "invalidParams[0].name", equalTo("title"),
        "invalidParams[0].reason", equalTo("タイトルは100文字以内です"));
    /*
     * {
     *   title: 'xxx',
     *   detail: 'xxxxxx',
     *   invalidParams: [
     *     {name: 'title', reason: 'タイトルは100文字以内です'}
     *   ]
     * }
     */
  }
}
