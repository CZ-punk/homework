package com.homework.basic.presentation.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final String message;

  public UserException(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
