package ru.skillbox.currency.exchange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.currency.exchange.exception.IdNotExistException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IdNotExistException.class)
  public ResponseEntity<String> handlerDbAccessException(IdNotExistException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

}
