package org.eliassantosfortes.bestmatchedrestaurants2esf.controller.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandlerImpl extends ResponseEntityExceptionHandler
    implements RestExceptionHandler {

  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request payload");
  }

  @ExceptionHandler(NumberFormatException.class)
  protected ResponseEntity<Object> handleNumberFormatException(
      NumberFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Non-numeric input received for numeric field");
  }
}
