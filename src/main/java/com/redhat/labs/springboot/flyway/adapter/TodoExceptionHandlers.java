package com.redhat.labs.springboot.flyway.adapter;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandlers {
  private static final Logger LOGGER = LoggerFactory.getLogger(TodoExceptionHandlers.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    LOGGER.debug("handle MethodArgumentNotValidException", ex);

    var invalidParams = ex.getAllErrors().stream().map(error -> {
      if (error instanceof FieldError) {
        var fieldError = (FieldError) error;
        return new ApiError.InvalidParam(fieldError.getField(), error.getDefaultMessage());
      } else {
        return new ApiError.InvalidParam(error.getObjectName(), error.getDefaultMessage());
      }
    }).collect(Collectors.toList());

    var apiError = new ApiError(
      "入力値エラー",
      "送信された入力内容にエラーがあります。詳細は invalidParams を参照してください。",
      invalidParams);

    return ResponseEntity
      .badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .body(apiError);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException ex) {
    LOGGER.debug("handle DateTimeParseException", ex);

    var apiError = new ApiError("日付書式エラー", ex.getMessage());

    return ResponseEntity
      .badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .body(apiError);
  }
}
