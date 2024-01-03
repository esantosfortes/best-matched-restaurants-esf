package org.eliassantosfortes.bestmatchedrestaurants2esf.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

  protected ResponseEntity<ErrorResponse> createBadRequestErrorResponse(String message) {
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  protected ResponseEntity<ErrorResponse> validateParameters(Object... params) {
    for (Object param : params) {
      if (param instanceof String && ((String) param).isEmpty()) {
        return createBadRequestErrorResponse("String parameter cannot be empty");
      } else if (param instanceof Integer) {
        int number = Integer.parseInt(param.toString());
        if (number < 0) {
          return createBadRequestErrorResponse("Integer parameter cannot be negative");
        }
      } else if (param instanceof Number) {
        double number = Double.parseDouble(param.toString());
        if (number < 0) {
          return createBadRequestErrorResponse("Double parameter cannot be negative");
        }
      }
    }
    return null;
  }

  boolean allParametersAreNull(Object... params) {
    for (Object param : params) {
      if (param != null) {
        return false;
      }
    }
    return true;
  }

  @Setter
  @Getter
  protected static class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
      this.message = message;
    }
  }
}
