package com.redhat.labs.springboot.flyway.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redhat.labs.springboot.flyway.application.Todo;
import com.redhat.labs.springboot.flyway.application.TodoRepository;

@Repository
public interface TodoJpaRepository extends TodoRepository, JpaRepository<Todo, Long> { }
