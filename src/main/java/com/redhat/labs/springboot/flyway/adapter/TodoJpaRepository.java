package com.redhat.labs.springboot.flyway.adapter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.redhat.labs.springboot.flyway.application.Todo;
import com.redhat.labs.springboot.flyway.application.TodoRepository;

@Repository
public interface TodoJpaRepository extends TodoRepository, JpaRepository<Todo, Long> {
  @Query("""
    SELECT t
    FROM Todo t
    WHERE t.done = 0
  """)
  List<Todo> findUnfinished();
}
