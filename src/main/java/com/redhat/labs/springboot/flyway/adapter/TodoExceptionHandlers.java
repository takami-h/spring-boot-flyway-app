package com.redhat.labs.springboot.flyway.adapter;

import java.time.format.DateTimeParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandlers {
  private static final Logger LOGGER = LoggerFactory.getLogger(TodoExceptionHandlers.class);

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException ex) {
    LOGGER.debug("handle DateTimeParseException", ex);

    var badRequest = ResponseEntity
      .status(HttpStatus.BAD_REQUEST.value())
      .contentType(MediaType.APPLICATION_JSON)
      .body(Map.of(
        "title", "failed to parse datetime",
        "detail", ex.getMessage()));

    return badRequest;
  }
}
